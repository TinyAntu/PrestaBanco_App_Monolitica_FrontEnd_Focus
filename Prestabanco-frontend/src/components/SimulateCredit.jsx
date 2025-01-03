import { useState } from 'react';
import {
  Box,
  FormControl,
  TextField,
  Button,
  Typography, MenuItem,
  Modal
} from '@mui/material';
import creditService from '../services/credit.service';

const SimulateCredit = () => {
    const [capital, setCapital] = useState("");
    const [years, setYears] = useState("");
    const [type, setType] = useState("");
    const [annual_interest, setAnnualInterest] = useState("");
    const [monthlyPayment, setMonthlyPayment] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [modalText, setModalText] = useState("");
    const [flag, setFlag] = useState(false);

    const simulateCredit = (e) => {
        setFlag(false); // Inicializamos en false
        if (!type) {
            alert("Por favor, seleccione algun tipo de crédito antes de simular.");
            return; // Detiene la ejecución si no se selecciona un tipo de crédito
        }
    
        e.preventDefault();
        console.log("Simulate credit", capital, "-", annual_interest, "-", years);
    
        creditService
            .simulate(capital, annual_interest, years)
            .then(response => {
                setFlag(true); // Se marca como éxito
                console.log("Results:", response.data);
                setMonthlyPayment(`Cuota Mensual: ${response.data} pesos`);
    
                // Lógica del modal cuando la simulación es exitosa
                let text = "";
                switch (type) {
                    case "1":
                        text = `Primera vivienda: Cuenta con un plazo máximo de 30 años.\n
              La tasa de interés anual se encuentra estipulada en la simulación.\n
              El monto máximo de financiamiento es de 80% del valor de la propiedad y tendrá que facilitar documentos:\n
              - Comprobante de ingresos.\n
              - Certificado de avalúo.\n
              - Historial crediticio.`;
                        break;
                    case "2":
                        text = `Segunda vivienda: Cuenta con un plazo máximo de 20 años.\n
              La tasa de interés anual se encuentra estipulada en la simulación.\n
              El monto máximo de financiamiento es de 70% del valor de la propiedad y tendrá que facilitar documentos:\n
              - Comprobante de ingresos.\n
              - Certificado de avalúo.\n
              - Escritura de la primera vivienda.\n
              - Historial crediticio.`;
                        break;
                    case "3":
                        text = `Propiedades comerciales: Cuenta con un plazo máximo de 25 años.\n
              La tasa de interés anual se encuentra estipulada en la simulación.\n
              El monto máximo de financiamiento es de 60% del valor de la propiedad y tendrá que facilitar documentos:\n
              - Estado financiero del negocio.\n
              - Comprobante de ingresos.\n
              - Certificado de avalúo.\n
              - Plan de negocios.`;
                        break;
                    case "4":
                        text = `Remodelación: Cuenta con un plazo máximo de 15 años.\n
              La tasa de interés anual se encuentra estipulada en la simulación.\n
              El monto máximo de financiamiento es de 50% del valor de la propiedad y tendrá que facilitar documentos:\n
              - Comprobante de ingresos.\n
              - Presupuesto de la remodelación.\n
              - Certificado de avalúo actualizado.`;
                        break;
                    default:
                        text = "";
                }
    
                setModalText(text);
                setShowModal(true); // Mostrar el modal
            })
            .catch(error => {
                setFlag(false); // Aseguramos que el flag siga en false
                // Aquí manejamos los errores
                
                if(capital === "") {
                    alert("Por favor, rellene el Capital antes de Simular.");
                }else if(annual_interest === "") {
                    alert("Por favor, rellene la Tasa de Interés antes de Simular.");
                }else if(years === "") {
                    alert("Por favor, rellene los Años antes de Simular.");
                    
                }else if (error.response && error.response.data) {
                    // Si el error tiene un mensaje, lo mostramos
                    console.log("Error:", error.response);
                    alert(`Error: ${error.response.data}`);
                }else {
                    // Si no, mostramos un mensaje por defecto
                    alert("Error desconocido, por favor intente más tarde.");
                }
                console.error("Error al Simular el credito", error);
            });
    };

    const handleCloseModal = () => setShowModal(false);

    //For type of credit to show the needed information
    const handleCreditype = (event) => {
      setType(event.target.value);
      setAnnualInterest("");
    };

    const getInterestLimits = () => {
      switch (type) {
          case "1":
              return { min: 3.5, max: 5 }; // First home
          case "2":
              return { min: 4, max: 6 }; // Second home
          case "3":
              return { min: 5, max: 7 }; // Commercial properties
          case "4":
              return { min: 4.5, max: 6 }; // Remodeling
          default:
              return { min: 0, max: 0 };
      }
    };

    const interestLimits = getInterestLimits();

    const handleAnnualInterestChange = (e) => {
      const value = e.target.value;
      const floatValue = parseFloat(value);

      // Check if the value is a number and within limits
      if (!isNaN(floatValue) && (floatValue >= interestLimits.min && floatValue <= interestLimits.max)) {
          setAnnualInterest(value);
      } else if (value === "") {
          // Allow empty input to reset the field
          setAnnualInterest(value);
      } else {
          // Show an alert or warning if the input is out of bounds
          alert(`La tasa de interés debe estar entre ${interestLimits.min} y ${interestLimits.max}.`);
      }
    };

    return (
      <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
          component="form"
          onSubmit={simulateCredit}
      >
          <Typography variant="h4" sx={{ marginTop: '30px', marginBottom: '20px', textAlign: 'center' }}>
              Simulación de Crédito
          </Typography>

          <FormControl margin="normal">
              <TextField
                  id="creditType"
                  label="Tipo de Crédito"
                  value={type}
                  select
                  variant="outlined"
                  sx={{ width: '650px' }}
                  onChange={handleCreditype}
              >
                  <MenuItem value="">Selecciona un tipo</MenuItem>
                  <MenuItem value="1">Primera vivienda</MenuItem>
                  <MenuItem value="2">Segunda vivienda</MenuItem>
                  <MenuItem value="3">Propiedades comerciales</MenuItem>
                  <MenuItem value="4">Remodelación</MenuItem>
              </TextField>
          </FormControl>

          <FormControl margin="normal">
              <TextField
                  id="capital"
                  label="Capital"
                  type="number"
                  value={capital}
                  variant="outlined"
                  sx={{ width: '650px' }}
                  onChange={(e) => setCapital(e.target.value)}
              />
          </FormControl>

          <FormControl margin="normal">
              <TextField
                  id="interest"
                  label="Interés Anual (%)"
                  type="number"
                  value={annual_interest}
                  variant="outlined"
                  sx={{ width: '650px' }}
                  placeholder={`Entre ${interestLimits.min} y ${interestLimits.max}`}
                  onChange={handleAnnualInterestChange}
                  inputProps={{
                      min: interestLimits.min,
                      max: interestLimits.max,
                      step: 0.5,
                  }}
              />
          </FormControl>

          <FormControl margin="normal">
              <TextField
                  id="years"
                  label="Años"
                  type="number"
                  value={years}
                  variant="outlined"
                  sx={{ width: '650px' }}
                  onChange={(e) => setYears(e.target.value)}
              />
          </FormControl>

          <Button variant="contained" color="primary" type="submit" style={{ marginTop: "1rem" }}>
              Simular Crédito
          </Button>

          {monthlyPayment && (
              <Typography variant="h6" style={{ marginTop: "1rem" }}>
                  {monthlyPayment}
              </Typography>
          )}

            <Modal
                open={showModal}
                onClose={handleCloseModal}
                aria-labelledby="modal-title"
                aria-describedby="modal-description"
            >
                <Box
                    sx={{
                        position: "absolute",
                        top: "50%",
                        left: "50%",
                        transform: "translate(-50%, -50%)",
                        bgcolor: "background.paper",
                        boxShadow: 24,
                        p: 4,
                        borderRadius: 2,
                        width: "700px",
                    }}
                >
                    <Typography id="modal-title" variant="h10" component="h2" sx={{ marginBottom: 2 }}>
                        Información del Crédito
                    </Typography>
                    <Typography id="modal-description" sx={{ whiteSpace: "pre-line" }}>
                        {modalText}
                    </Typography>
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "flex-end",
                            marginTop: 2,
                            
                        }}
                    >
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleCloseModal}
                            sx={{ marginTop: 2 }}
                           
                        >   
                            Cerrar
                        </Button>
                    </Box>
                </Box>
            </Modal>
      </Box>
  );
};

export default SimulateCredit;
