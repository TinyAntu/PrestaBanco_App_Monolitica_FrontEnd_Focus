import { useState } from "react";
import userService from "../services/user.service";
import { useNavigate } from "react-router-dom";
import { Box, Typography, FormControl, TextField, Button } from "@mui/material";

const UserLogin = () => {
  const [rut, setRut] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const login = (e) => {
    e.preventDefault();
    const user = { rut, password };

    userService
      .login(user)
      .then((response) => {
        console.log("Usuario autenticado exitosamente", response.data);
        // Store userId as a number in localStorage
        localStorage.setItem("userId", Number(response.data.id));
        localStorage.setItem("userTypeId", Number(response.data.role));
        console.log("API Response:", response.data.role);
        alert("Inicio de sesion correcto");
        navigate("/home"); 
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          alert("Contraseña incorrecta");
        } else if (error.response && error.response.status === 404) {
          alert("Usuario no encontrado");
        } else {
          console.error("Error al autenticar el usuario", error);
          alert("Ocurrió un error al iniciar sesión. Por favor, intente nuevamente.");
        }
      });
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      component="form"
      onSubmit={login}
    >
      <Typography variant="h4" sx={{ marginTop: '30px', marginBottom: '20px', textAlign: 'center' }}>
        Iniciar Sesión
      </Typography>

      <FormControl margin="normal">
        <TextField
          id="rut"
          label="RUT"
          type="text"
          value={rut}
          variant="outlined"
          sx={{ width: '650px' }}
          onChange={(e) => setRut(e.target.value)}
        />
      </FormControl>

      <FormControl margin="normal">
        <TextField
          id="password"
          label="Contraseña"
          type="password"
          value={password}
          variant="outlined"
          sx={{ width: '650px' }}
          onChange={(e) => setPassword(e.target.value)}
        />
      </FormControl>

      <Button variant="contained" color="primary" type="submit" sx={{ marginTop: "1rem", width: '650px' }}>
        Iniciar Sesión
      </Button>
    </Box>
  );
};

export default UserLogin;

