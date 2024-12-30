import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import creditService from "../services/credit.service";
import documentService from '../services/document.service';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";

const CreditEvaluation = () => {
  const [credits, setCredits] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [openDetailsDialog, setOpenDetailsDialog] = useState(false);
  const [selectedCredit, setSelectedCredit] = useState(null);
  const [evaluationResult, setEvaluationResult] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [responses, setResponses] = useState([null, null, null, null, null]); // For the questions in the level seven
  const [score, setScore] = useState(0); // To determine if the credit is approved or not, we need something else
  const navigate = useNavigate();

  const init = () => {
    creditService
      .getAll()
      .then((response) => {
        setCredits(response.data);
      })
      .catch((error) => {
        console.log("Error al obtener créditos.", error);
      });
  };

  

  const handleEvaluateClick = (credit) => {
    setSelectedCredit(credit);

    if (credit.level === 1) {
      creditService.evaluateStep1(credit.idCredit)
        .then((response) => {
          setEvaluationResult(response.data);
          setOpenDialog(true);
        })
        .catch((error) => {
          console.log("Error al evaluar el crédito.", error);
        });
    }
    
    if (credit.level === 2) {
          setOpenDialog(true);
          setEvaluationResult(true);
    }
    if (credit.level === 3) {
          setOpenDialog(true);
          setEvaluationResult(true);
    }

    if (credit.level === 4) {
      creditService.evaluateStep4(credit.idCredit)
        .then((response) => {
          setEvaluationResult(response.data);
          setOpenDialog(true);
        })
        .catch((error) => {
          console.log("Error al evaluar el crédito.", error);
        });
    }

    if (credit.level === 5) {
      creditService.evaluateStep5(credit.idCredit)
        .then((response) => {
          setEvaluationResult(response.data);
          setOpenDialog(true);
        })
        .catch((error) => {
          console.log("Error al evaluar el crédito.", error);
        });
    }

    if (credit.level === 6) {
      creditService.evaluateStep6(credit.idCredit)
        .then((response) => {
          setEvaluationResult(response.data);
          setOpenDialog(true);
        })
        .catch((error) => {
          console.log("Error al evaluar el crédito.", error);
        });
    }
    if(credit.level === 7){
      setResponses([null, null, null, null, null]); // Reset responses for new evaluation
      setScore(0);
      setOpenDialog(true);
    }
  };

  const handleResponseChange = (index, value) =>{
    const updatedResponses = [...responses];
    updatedResponses[index] = value;
    setResponses(updatedResponses);
    
    const updatedScore = updatedResponses.reduce((acc, curr) => acc + (curr === "Cumple" ? 1 : 0), 0);
    setScore(updatedScore);
  };

  const handleViewMoreClick = (credit) => {
    setSelectedCredit(credit);
    setOpenDetailsDialog(true);
  };

  const fetchDocuments = (creditId) => {
    documentService.getDocumentsByCreditId(creditId)
        .then(response => {
            setDocuments(response.data);
        })
        .catch(error => {
            console.log("Error Consiguiendo los Documentos:", error);
        });
  };

  const handleDownloadDocument = (documentId) => {
    documentService.downloadDocument(documentId)
      .then(response => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'document.pdf'); // Cambiar si se desea usar el nombre real del archivo
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
      .catch(error => {
        console.log("Error al descargar el documento.", error);
      });
  };

  const handleLevelUp = () => {

    const userConfirmed = window.confirm("¿Está seguro de que desea avanzar al siguiente nivel este crédito?");
        if (!userConfirmed) {
            return; // Detener si el usuario no confirma
        }

    if (selectedCredit && evaluationResult === true) {
      const updatedCredit = { ...selectedCredit, level: selectedCredit.level + 1 };

      creditService.update(selectedCredit.idCredit, updatedCredit)
        .then(() => {
          alert("Crédito subido de nivel correctamente");
          console.log("Level up successful");
          init();
          setOpenDialog(false);
        })
        .catch((error) => {
          alert("Error de conexion al intentar subir de nivel el crédito.");
          console.log("Error while leveling up", error);
        });
    }
  };

  const handleAprove = () =>{
    const userConfirmed = window.confirm("¿Está seguro de que desea aprovar este crédito?");
        if (!userConfirmed) {
            return; // Detener si el usuario no confirma
        }
    if (selectedCredit) {
      const updatedCredit = { ...selectedCredit, state: true, e: 4 };
      creditService.update(selectedCredit.idCredit, updatedCredit)
        .then(() => {
          alert("Crédito ha sido aprobado correctamente");
          console.log("Aproved credit successfully");
          init();
          setOpenDialog(false);
        })
        .catch((error) => {
          alert("Error de conexion al aprobar el crédito intente nuevamente.");
          console.log("Error while aproving the credit", error);
        });
    }
  };

  const handleReject = () =>{

    const userConfirmed = window.confirm("¿Está seguro de que desea cancelar este crédito?");
        if (!userConfirmed) {
            return; // Detener si el usuario no confirma
        }
    if (selectedCredit) {
      const updatedCredit = { ...selectedCredit, state: false, e: 7 };
      

      creditService.update(selectedCredit.idCredit, updatedCredit)
        .then(() => {
          alert("Crédito ha sido rechazado rechazado correctamente");
          console.log("Crédito rechazado");
          init();
          setOpenDialog(false);
        })
        .catch((error) => {
          alert("Error de conexion al intentar rechazar el crédito.");
          console.log("Error al intentar rechazar el crédito.", error);
        });
    }
  };

  
  useEffect(() => {
    if (selectedCredit) {
      fetchDocuments(selectedCredit.idCredit);
    }
  }, [selectedCredit]);

  useEffect(() => {
    const userTypeId = localStorage.getItem("userTypeId");
    if (userTypeId !== "2") {
      navigate("/home");
      alert("Esta es una zona solo para ejecutivos");
    } else {
      init();
    }
  }, [navigate]);

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
                  {(credit.state === null) && (  // Verifica que credit.state sea null
                    <Button variant="contained" color="primary" size="small" onClick={() => handleEvaluateClick(credit)}>
                      Evaluar
                    </Button>
                  )}

                  <Button 
                    variant="outlined" 
                    sx={{ color: "white", backgroundColor: "#333", "&:hover": { backgroundColor: "#555" } }} 
                    size="small" 
                    onClick={() => handleViewMoreClick(credit)}
                  >
                    Ver Más
                  </Button>

                </TableCell>

              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false) } fullWidth>
        <DialogTitle>Evaluar Crédito</DialogTitle>
        <DialogContentText sx={{ padding: '20px' }} >
            {selectedCredit && (
                <> 
                    {/* Mensajes específicos según el nivel */}
                    {evaluationResult !== null && selectedCredit.level === 1 && (
                        <p>
                            {evaluationResult
                                ? "Se cumple con el requerimiento de cuota/ingreso <= 35%."
                                : "No se cumple con el requerimiento de cuota/ingreso <= 35%."}
                        </p>
                    )}

                    {evaluationResult !== null && selectedCredit.level === 2 && (
                        <p>
                            {"Determine el estado de DICOM con el historial crediticio"}
                        </p>
                    )}

                    {evaluationResult !== null && selectedCredit.level === 3 && (
                        <p>
                            {"Determine la estabilidad laboral con el documento pertinente"}
                        </p>
                    )}

                    {evaluationResult !== null && selectedCredit.level === 4 && (
                        <p>
                            {evaluationResult
                                ? "Se cumple con el requerimiento de suma de deudas es menor al 50% de los ingresos."
                                : "La suma de las deudas excede el 50% del ingresos."}
                        </p>
                    )}

                    {evaluationResult !== null && selectedCredit.level === 5 && (
                        <p>
                            {evaluationResult
                                ? "El monto de financiamineto no excede el maximo designado para el tipo de credito."
                                : "El monto de financiamineto excede el maximo designado para el tipo de credito."}
                        </p>
                    )}

                    {evaluationResult !== null && selectedCredit.level === 6 && (
                        <p>
                            {evaluationResult
                                ? "No se encuentra cerca a la edad maxima '75 años'."
                                : "Se encuentra muy cercano a la edad maxima '75 años'."}
                        </p>
                    )}

                    {selectedCredit.level === 7 && (
                      <>

                      <p>Para determinar la capacidad de ahorro revise el documento de capacidad de ahorro y verifique los siguientes requisitos</p>
                      {["El cliente debe tener un saldo mínimo en su cuenta de ahorros o inversiones que sea al menos el 10% del monto del préstamo solicitado.",
                        "El cliente debe haber mantenido un saldo positivo en su cuenta de ahorros por lo menos durante los últimos 12 meses, sin realizar retiros significativos (> 50% del saldo)",
                        "El cliente debe realizar depósitos regulares en su cuenta de ahorros o inversión. Se consideran regulares aquellos realizados con una frecuencia mensual o trimestral (el monto minimo de los depositos deben sumar al menos 5% de sus ingresos mensuales)",
                        "Si el cliente tiene menos de 2 años en su cuenta de ahorros, debe tener un saldo acumulado que sea al menos el 20% del monto del préstamo solicitado (si el cliente tiene 2 años o más con la cuenta, un saldo acumulado del 10%será suficiente).",
                        "Si el cliente ha realizado un retiro superior al 30% del saldo de su cuenta en los últimos 6 meses, marcar este punto como negativo, ya que indica una posible falta de estabilidad financiera."].map((question, index) => (
                          <div key={index}>
                            <p>{question}</p>
                            <Button
                              variant={responses[index] === "Cumple" ? "contained" : "outlined"}
                              onClick={() => handleResponseChange(index, "Cumple")}
                            >
                              Cumple
                            </Button>
                            <Button
                              variant={responses[index] === "No Cumple" ? "contained" : "outlined"}
                              onClick={() => handleResponseChange(index, "No Cumple")}
                            >
                              No Cumple
                            </Button>
                          </div>
                      ))}
                      <p>
                        Resultado: {score} de 5 preguntas cumplen los requisitos.
                        <br />
                        {score == 5  ? "Aprobacion : Capacidad de ahorro 'solida'." :
                        score == 3 || score == 4 ? "Revision Adicional: Capacidad de ahorro 'moderada'" :
                        "Rechazo: Capacidad de ahorro 'insuficiente'."
                        }
                      </p>
                    </>

                    )}

                    <div>
                        <h4>Documentos Asociados:</h4>
                          {documents.map(doc => (
                          <Button
                          key={doc.id}
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => handleDownloadDocument(doc.id)}
                          >
                            Descargar {doc.filename}
                          </Button>
                      ))}
                    </div>
                    {/* Agrega más evaluaciones para otros niveles aquí */}
                </>
            )}
        </DialogContentText>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="secondary">
            Cancelar
          </Button>
          
          

          {!(selectedCredit && selectedCredit.level === 7) && (
            <Button
              onClick={handleLevelUp}
              color="primary"
              variant="contained"
              disabled={!evaluationResult}
            >
              Incrementar Nivel
            </Button>
          )}

          <Button
            onClick={handleAprove}
            color="primary"
            variant="contained"
            disabled={!(selectedCredit && selectedCredit.level === 7 && score === 5)}
          >
            Aprobacion
          </Button>
          

          <Button
            onClick={handleReject}
            color="primary"
            variant="contained"
          >
            Rechazar
          </Button>

        </DialogActions>
      </Dialog>
      
      <Dialog open={openDetailsDialog} onClose={() => setOpenDetailsDialog(false)} fullWidth>
        <DialogTitle>Detalles del Crédito</DialogTitle>
        <DialogContent>
          {selectedCredit && (
            <>
              <p><strong>Id Crédito:</strong> {selectedCredit.idCredit}</p>
              <p><strong>Cantidad:</strong> {selectedCredit.capital}</p>
              <p><strong>Ingresos:</strong> {selectedCredit.income}</p>
              <p><strong>Valor de la Propiedad:</strong> {selectedCredit.property_value}</p>
              <p><strong>Deuda:</strong> {selectedCredit.debt}</p>
              <p><strong>Interés Anual:</strong> {selectedCredit.annual_interest}</p>
              <p><strong>Años:</strong> {selectedCredit.years}</p>
              <p><strong>Tipo:</strong> {
                selectedCredit.type === 1 ? "Primera Vivienda" :
                selectedCredit.type === 2 ? "Segunda Vivienda" :
                selectedCredit.type === 3 ? "Propiedades Comerciales" :
                selectedCredit.type === 4 ? "Remodelación" :
                "Tipo Desconocido"
              }</p>
              <p><strong>Estado:</strong> {selectedCredit.state ? "Aprobado" : selectedCredit.state === null ? "En Revisión" : "Rechazado"}</p>
              {/* Add more fields as necessary */}
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDetailsDialog(false)} color="primary">Cerrar</Button>
        </DialogActions>
      </Dialog>

    </>
  );
};

export default CreditEvaluation;
