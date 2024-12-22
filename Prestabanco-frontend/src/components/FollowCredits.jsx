import { useState, useEffect } from "react";
import creditService from "../services/credit.service";
import documentService from '../services/document.service';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Dialog, DialogActions, Typography, DialogContentText,DialogContent, DialogTitle, TextField } from "@mui/material";

function FollowCredits() {
    const [userId, setUserId] = useState(null);
    const [credits, setCredits] = useState([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [openTotalCostDialog, setOpenTotalCostDialog] = useState(false);
    const [selectedCredit, setSelectedCredit] = useState(null);
    const [evaluationResult, setEvaluationResult] = useState(null);
    const [totalCost, setTotalCost] = useState(null);
    const [documents, setDocuments] = useState({
        file1: null,
        file2: null,
        file3: null,
    });

    useEffect(() => {
        const storedUserId = localStorage.getItem("userId");
        setUserId(storedUserId ? Number(storedUserId) : null);
    }, []);
    
    useEffect(() => {
        if (userId) {
            init(); // Fetch credits when userId is set
        }
    }, [userId]);

    const init = () => {
        if (!userId) return; 
        creditService
            .getAllById(userId)
            .then((response) => {
                setCredits(response.data);
            })
            .catch((error) => {
                console.error("Error al obtener créditos.", error);
            });
    };

    const handleDocumentChange = (e, docType) => {
        const file = e.target.files[0];
        if (docType === 'file1') {
            setDocuments((prev) => ({ ...prev, file1: file }));
        } else if (docType === 'file2') {
            setDocuments((prev) => ({ ...prev, file2: file }));
        } else if (docType === 'file3') {
            setDocuments((prev) => ({ ...prev, file3: file }));
        }
    };

    const handleFollowClick = (credit) => {
        setSelectedCredit(credit);

        if (credit.e === 1) {
            creditService.follow1(credit.idCredit)
                .then((response) => {
                    setEvaluationResult(response.data);
                    setOpenDialog(true);
                })
                .catch((error) => {
                    console.log("Error al evaluar el crédito.", error);
                });
        } else if (credit.e === 2) {
            setOpenDialog(true); // Open dialog for uploading documents
            setEvaluationResult(false); // Set to false to disable the "Pasar de etapa" button
        } else if (credit.e === 3) {
            setOpenDialog(true); // Open dialog for the wait till e == 4
            setEvaluationResult(false); // Set to false to disable the "Pasar de etapa" button
        } else if(credit.e == 4){
            setOpenDialog(true); 
            setEvaluationResult(true); // Set to true to enable the "Pasar de etapa" button

        } else if(credit.e == 5){
            setOpenDialog(true); 
            setEvaluationResult(true); // Set to true to enable the "Pasar de etapa" button

        } else if(credit.e == 6){
            setOpenDialog(true); 
            setEvaluationResult(true); // Set to true to enable the "Pasar de etapa" button
        } else if(credit.e == 7){
            setOpenDialog(true); 
            setEvaluationResult(false); // Set to true to enable the "Pasar de etapa" button
        } else if (credit.e === 8) {
            setOpenDialog(true); 
            setEvaluationResult(false); // Set to false to disable the "Pasar de etapa" button
        } else if (credit.e === 9) {
            setOpenDialog(true); 
            setEvaluationResult(false); // Set to false to disable the "Pasar de etapa" button
        }

    };

    const handleStageUp = () => {
        if (selectedCredit && evaluationResult === true) {
            const updatedCredit = { ...selectedCredit, e: selectedCredit.e + 1 };
    
            creditService.update(selectedCredit.idCredit, updatedCredit)
                .then(() => {
                    console.log("Etapa del credito aumentada");
                    init();
                    setOpenDialog(false);
                })
                .catch((error) => {
                    console.log("Error al intentar incrementar la etapa del crédito.", error);
                });
        }
    };

    const handleTotalCost = (credit) => {
        if (selectedCredit) {
            creditService.totalCost(credit.idCredit)
                .then((response) => {
                    setTotalCost(response.data);
                    setOpenTotalCostDialog(true); 
                    console.log("The total cost is:", response.data);
                })
                .catch((error) => {
                    console.error("Error while calculating the total cost:", error);
                });
        }
    };

    const handleAprove = () => {
        if (selectedCredit ) {
            const updatedCredit = { ...selectedCredit, e: 9 };
    
            creditService.update(selectedCredit.idCredit, updatedCredit)
                .then(() => {
                    console.log("This credit is aproved successfully"); 
                    init();
                    setOpenDialog(false);
                })
                .catch((error) => {
                    console.log("Error while aproving the credit.", error);
                });
        }
    };


    const handleCancel= () => {
        if(selectedCredit){
            const updatedCredit = { ...selectedCredit, e: 8, state: false };
            creditService.update(selectedCredit.idCredit, updatedCredit)
                .then(() => {
                    console.log("Credito cancelado");
                    init();
                    setOpenDialog(false);
                })
                .catch((error) => {
                    console.log("Error al intentar cancelar el crédito.", error);
                });
        }
    };

    const uploadDocuments = async (creditId) => {
        const { file1, file2, file3 } = documents;
    
        if (file1 && file2 && file3) {
            const documentData1 = {
                file: await convertToBase64(file1),
                doc_type: 'comprobante_ingresos',
                filename: file1.name,
                idCredit: creditId,
            };
    
            const documentData2 = {
                file: await convertToBase64(file2),
                doc_type: 'certificado_avaluo',
                filename: file2.name,
                idCredit: creditId,
            };
    
            const documentData3 = {
                file: await convertToBase64(file3),
                doc_type: 'capacidad_ahorro',
                filename: file3.name,
                idCredit: creditId,
            };
    
            try {
                await Promise.all([
                    documentService.create(documentData1),
                    documentService.create(documentData2),
                    documentService.create(documentData3),
                ]);
    
                alert("Documents uploaded successfully.");
                setEvaluationResult(true);
    
                // Update the credit stage when the docs are uploaded
                const updatedCredit = { ...selectedCredit, e: selectedCredit.e + 1 };
                await creditService.update(selectedCredit.idCredit, updatedCredit);
    
                //To refresh it
                setCredits((prevCredits) =>
                    prevCredits.map((credit) =>
                        credit.idCredit === selectedCredit.idCredit
                            ? { ...credit, e: updatedCredit.e }
                            : credit
                    )
                );
                setSelectedCredit(updatedCredit);
            } catch (error) {
                console.error("Error uploading documents:", error);
                alert("Error uploading documents. Please try again.");
            }
        } else {
            alert("Please upload all required documents.");
        }
    };

    const convertToBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = () => resolve(reader.result.split(',')[1]); // Base64
            reader.onerror = reject;
        });
    };

    const handleSubmitDocuments = () => {
        if (selectedCredit) {
            uploadDocuments(selectedCredit.idCredit);
            setOpenDialog(false); // Close dialog after submission
        }
    };

    return (
        <>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="left">Cantidad</TableCell>
                            <TableCell align="left">Interés</TableCell>
                            <TableCell align="right">Nivel</TableCell>
                            <TableCell align="right">Etapa</TableCell>
                            <TableCell align="right">Tipo</TableCell>
                            <TableCell align="right">Estado</TableCell>
                            <TableCell align="center">Acciones</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {credits.map((credit) => (
                            <TableRow key={credit.idCredit}>
                                <TableCell align="left">{credit.capital}</TableCell>
                                <TableCell align="left">{credit.annual_interest}</TableCell>
                                <TableCell align="right">{credit.level}</TableCell>

                                <TableCell align="right">
                                {credit.e === 1 && "Revision Inicial"}
                                {credit.e === 2 && "Pendiente de Documentacion"}
                                {credit.e === 3 && "En Evaluacion"}
                                {credit.e === 4 && "Pre-Aprobada"}
                                {credit.e === 5 && "En Aprobacion Final"}
                                {credit.e === 6 && "Aprobada"}
                                {credit.e === 7 && "Rechazada"}
                                {credit.e === 8 && "Cancelada por el Cliente"}
                                {credit.e === 9 && "En Desembolso"}
                                {![1, 2, 3, 4,5,6,7,8,9].includes(credit.e) && "Etapa Desconocida"}
                                </TableCell>
                                
                                <TableCell align="right">
                                {credit.type == 1 && "Primer Vivienda"}
                                {credit.type == 2 && "Segunda Vivienda"}
                                {credit.type == 3 && "Propiedades Comerciales"}
                                {credit.type == 4 && "Remodelacion"}
                                {![1, 2, 3, 4].includes(credit.type) && "Tipo Desconocido"}
                                </TableCell>


                                <TableCell align="right">{credit.state ? "Aprobado" : credit.state === null ? "En Revisión" : "Rechazado"}</TableCell>

                                <TableCell align="center">
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        size="small"
                                        onClick={() => handleFollowClick(credit)}
                                    >
                                        Consultar
                                        
                                    </Button>
                                    {(credit.e === 9 || credit.e === 4 || credit.e === 6) && (
                                    <Button
                                        variant="contained"
                                        color="secondary"
                                        size="small"
                                        onClick={() => handleTotalCost(credit)} 
                                        sx={{ marginLeft: '8px' }} 
                                    >
                                        Costos Totales
                                    </Button>
                                    )}

                                    {credit.e < 6 && ( // Condintion until the user can cancel the credit
                                        <Button 
                                            variant="outlined" 
                                            sx={{ color: "white", backgroundColor: "#333", "&:hover": { backgroundColor: "#555" } }} 
                                            size="small" 
                                            onClick={() => handleCancel(credit)}
                                        >
                                            Cancelar
                                        </Button>
                                    )}

                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {/* Dialog for the total cost */}
            <Dialog open={openTotalCostDialog} onClose={() => setOpenTotalCostDialog(false)}>
                <DialogTitle>Costos Totales</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {totalCost !== null 
                            ? `El costo total calculado es: $${totalCost}`
                            : "Calculando el costo total..."}
                    </DialogContentText>

                    <DialogContentText sx={{ marginTop: '16px' }}>
                        Esta suma considera los seguros de degravamen y de incendio donde:
                        <br /><br />
                        El seguro de incendios tiene un costo fijo de: $20,000 mensuales.
                        <br />
                        El seguro de desgravamen es: monto del préstamo * 0.03 $ mensuales.
                        <br /><br />
                        Donde el costo mensual es: 
                        Cuota mensual del préstamo + seguro de incendios + seguro de desgravamen.
                        <br /><br />
                        Además, dentro del costo total se asume una comisión por administración de: 
                        monto del préstamo * 0.01.
                        <br /><br />
                        Donde el costo final es: 
                        Costo mensual * (Años del préstamo * 12) + comisión por administración.
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button onClick={() => setOpenTotalCostDialog(false)} color="secondary">
                        Cerrar
                    </Button>
                </DialogActions>
            </Dialog>
    
            {/* Text for the levels */}
            <Dialog open={openDialog} onClose={() => setOpenDialog(false)} fullWidth>
                <DialogTitle>Consultar credito</DialogTitle>
                <DialogContentText sx={{ padding: '20px' }}>
                    {selectedCredit && (
                        <>
                            {/* Messages specific to the evaluation result */}
                            {evaluationResult !== null && selectedCredit.e === 1 && (
                                <p>
                                    {evaluationResult
                                        ? "Se completaron los campos requeridos."
                                        : "No se completaron los campos requeridos."}
                                </p>
                            )}

                            {selectedCredit.e === 2 && (
                                <>
                                    <TextField
                                        type="file"
                                        accept=".pdf"
                                        onChange={(e) => handleDocumentChange(e, 'file1')}
                                        style={{ margin: '16px 0' }}
                                    />
                                    <Typography>Comprobante de ingresos</Typography>

                                    <TextField
                                        type="file"
                                        accept=".pdf"
                                        onChange={(e) => handleDocumentChange(e, 'file2')}
                                        style={{ margin: '16px 0' }}
                                    />
                                    <Typography>Certificado de avalúo</Typography>

                                    <TextField
                                        type="file"
                                        accept=".pdf"
                                        onChange={(e) => handleDocumentChange(e, 'file3')}
                                        style={{ margin: '16px 0' }}
                                    />
                                    <Typography>Capacidad de ahorro</Typography>
                                </>
                            )}

                            {selectedCredit.e === 3 && (
                                <p>
                                    {"Espere hasta que un ejecutivo realize las comprobaciones necesarias."}
                                </p>
                            )}

                            {selectedCredit.e === 4 && (
                                <>
                                    <p>
                                        <strong>Tipo de Crédito:</strong>{" "}
                                        {selectedCredit.type === 1
                                            ? "Primera vivienda"
                                            : selectedCredit.type === 2
                                            ? "Segunda vivienda"
                                            : selectedCredit.type === 3
                                            ? "Propiedades comerciales"
                                            : selectedCredit.type === 4
                                            ? "Remodelación"
                                            : "Tipo de crédito desconocido"}
                                    </p>

                                    {selectedCredit.type === 1 && (
                                        <p>Condiciones del crédito para Primera vivienda: <br />
                                            - Plazo máximo de 30 años.<br />
                                            - Tasa de interés anual 3.5% - 5.0%.<br />
                                            - Monto de financiamiento máximo del 80% del valor de la propiedad.
                                        </p>
                                    )}

                                    {selectedCredit.type === 2 && (
                                        <p>Condiciones del crédito para Segunda vivienda: <br />
                                            - Plazo máximo de 20 años.<br />
                                            - Tasa de interés anual 4.0% - 6.0%.<br />
                                            - Monto de financiamiento máximo del 70% del valor de la propiedad.
                                        </p>
                                    )}

                                    {selectedCredit.type === 3 && (
                                        <p>Condiciones del crédito para Propiedades comerciales: <br />
                                            - Plazo máximo de 25 años.<br />
                                            - Tasa de interés anual 5.0% - 7.0%.<br />
                                            - Monto de financiamiento máximo del 60% del valor de la propiedad.
                                        </p>
                                    )}

                                    {selectedCredit.type === 4 && (
                                        <p>Condiciones del crédito para Remodelación: <br />
                                            - Plazo máximo de 15 años.<br />
                                            - Tasa de interés anual 4.5% - 6.0%.<br />
                                            - Monto de financiamiento máximo del 50% del valor de la propiedad.
                                        </p>
                                    )}
                                </>
                            )}

                            {selectedCredit.e === 5 && (
                                <p>
                                    {"Se revisan los detalles finales, se emiten los contratos y se preparan los documentos legales. "}
                                </p>
                            )}

                            {selectedCredit.e === 6 && (
                                <div>
                                    <p>
                                        {"Se confirma la aprobacion del credito."}
                                    </p>
                                    <label htmlFor="contract-signing-date">Seleccione una fecha para la firma del contrato:</label>
                                    <input 
                                        type="date" 
                                        id="contract-signing-date" 
                                        onChange={(e) => console.log("Fecha seleccionada:", e.target.value)} 
                                    />
                                </div>
                            )}

                            {selectedCredit.e === 7 && (
                                <>
                                    <p>
                                        <strong>La solicitud se rechazo en el nivel:</strong>{" "}
                                        {selectedCredit.level === 1
                                            ? "Relación Cuota/Ingreso"
                                            : selectedCredit.level === 2
                                            ? "Historial Crediticio del Cliente"
                                            : selectedCredit.level === 3
                                            ? "Antigüedad Laboral y Estabilidad"
                                            : selectedCredit.level === 4
                                            ? "Relación Deuda/Ingreso"
                                            : selectedCredit.level === 5
                                            ? "Monto Máximo de Financiamiento"
                                            : selectedCredit.level === 6
                                            ? "Edad del Solicitante"
                                            : selectedCredit.level === 7
                                            ? "Capacidad de Ahorro"
                                            : "Rechazo desconocido"}
                                    </p>

                                    {selectedCredit.level === 1 && (
                                        <p> La relación cuota/ingreso es mayor que el umbral establecido por el banco (35%).
                                        </p>
                                    )}

                                    {selectedCredit.level === 2 && (
                                        <p>Existen morosidades graves o una alta cantidad de deudas pendientes.
                                        </p>
                                    )}

                                    {selectedCredit.level === 3 && (
                                        <p>Sus datos no cumplem con la antiguedad laboral o estabilidad necesarias.
                                        </p>
                                    )}

                                    {selectedCredit.level === 4 && (
                                        <p>Las deudas superar el 50% de los ingresos mensuales.
                                        </p>
                                    )}

                                    {selectedCredit.level === 5 && (
                                        <p>El monto solicitado supera el maximo establecido del valor de la propiedad.
                                        </p>
                                    )}

                                    {selectedCredit.level === 6 && (
                                        <p>La edad al final el prestamo se encuentra muy cercano a la edad maxima permitida.
                                        </p>
                                    )}

                                    {selectedCredit.level === 7 && (
                                        <p>Cuenta con una capacidad de ahorro insuficiente.
                                        </p>
                                    )}

                                </>
                            )}

                            {selectedCredit.e === 8 && (
                                <p>
                                    {"El cliente ha decidido cancelar la solicitud de  crédito."}
                                </p>
                            )}

                            {selectedCredit.e === 9 && (
                                <p>
                                    {"La solicitud ha sido aprobada y se está ejecutando el proceso de desembolso del monto aprobado."}
                                </p>
                            )}

                        </>
                    )}
                </DialogContentText>
                <DialogActions>
                    <Button onClick={() => setOpenDialog(false)} color="secondary">
                        Cancelar
                    </Button>

                    {selectedCredit && selectedCredit.e === 2 && (
                        <Button
                            onClick={handleSubmitDocuments}
                            color="primary"
                            variant="contained"
                        >
                            Subir Documentos
                        </Button>
                    )}

                    {selectedCredit && selectedCredit.e !== 2 && selectedCredit.e !== 9 && selectedCredit.e !== 7 && (
                        <>
                            {selectedCredit.e !== 6 ? (
                                <Button
                                    onClick={handleStageUp}
                                    color="primary"
                                    variant="contained"
                                    disabled={!evaluationResult} // Disable if evaluation condition is not met
                                >
                                    {selectedCredit.e === 4 ? "Aceptar" : "Pasar de etapa"}
                                </Button>
                            ) : (
                                <Button
                                    onClick={handleAprove} // Maneja la acción de desembolsar
                                    color="secondary" // Puedes elegir otro color
                                    variant="contained"
                                >
                                    Desembolsar
                                </Button>
                            )}
                        </>
                    )}

                    


                </DialogActions>          
            </Dialog>
        </>
    );
}

export default FollowCredits;

