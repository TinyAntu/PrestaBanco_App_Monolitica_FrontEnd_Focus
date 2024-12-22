import { useState } from 'react';
import {
  Box,
  FormControl,
  TextField,
  Button,
  Typography, MenuItem
} from '@mui/material';
import creditService from '../services/credit.service';

const SimulateCredit = () => {
    const [capital, setCapital] = useState("");
    const [years, setYears] = useState("");
    const [type, setType] = useState("");
    const [annual_interest, setAnnualInterest] = useState("");
    const [additionalText, setAdditionalText] = useState("");
    const [monthlyPayment, setMonthlyPayment] = useState(null);

    const simulateCredit = (e) => {
      e.preventDefault();
      console.log("Simulate credit", capital, "-", annual_interest, "-", years);
      creditService
        .simulate(capital, annual_interest, years)
        .then(response => {
          console.log("Results:", response.data);
          setMonthlyPayment(`Monthly share: ${response.data} pesos`);
        })
        .catch(error => {
          console.error("Error while simulating:", error);
          setMonthlyPayment("Error please try again.");
        });
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
          setAdditionalText(text);
    };

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

          {additionalText && (
              <Typography variant="h6" sx={{ whiteSpace: 'pre-line', marginTop: 2 }}>
              {additionalText}
            </Typography>
          )}
      </Box>
  );
};

export default SimulateCredit;
