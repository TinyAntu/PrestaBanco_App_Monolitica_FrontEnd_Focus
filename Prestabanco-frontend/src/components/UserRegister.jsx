import { useState } from "react";
import userService from "../services/user.service";
import { Box, Typography, FormControl, TextField, Button, Checkbox, FormControlLabel } from "@mui/material";
import { useNavigate } from "react-router-dom";

const UserRegister = () => {
  const [rut, setRut] = useState("");
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState(false);
  const [birthdate, setBirthdate] = useState("");

  const navigate = useNavigate();
  const saveUser = (e) => {
    e.preventDefault();
    const userRole = role ? 2 : 1; 
    const user = { rut, email, name, password, role: userRole, birthdate };

    userService
    .register(user)
      .then((response) => {
        const userId = response.data.id;
        console.log("Usuario registrado exitosamente", response.data);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userTypeId", userRole);
        console.log("API Response:", response);
        alert("Usuario registrado correctamente");
        navigate("/home");
      })
      .catch((error) => {
        // Aquí manejamos los errores
        if (error.response && error.response.data) {
          // Si el error tiene un mensaje, lo mostramos
          alert(`Error: ${error.response.data}`);
        } else {
          // Si no, mostramos un mensaje por defecto
          alert("Error desconocido, por favor intente más tarde.");
        }
        console.error("Error al registrar el usuario", error);
      });

  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      component="form"
      onSubmit={saveUser}
    >
      <Typography variant="h4" sx={{ 
        marginTop: '30px',
        marginBottom: '20px',
        textAlign: 'center',
      }}>
        Registro de Usuario
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
          id="email"
          label="Email"
          type="email"
          value={email}
          variant="outlined"
          sx={{ width: '650px' }}
          onChange={(e) => setEmail(e.target.value)}
        />
      </FormControl>

      <FormControl margin="normal">
        <TextField
          id="name"
          label="Nombre"
          type="text"
          value={name}
          variant="outlined"
          sx={{ width: '650px' }}
          onChange={(e) => setName(e.target.value)}
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

      <FormControl margin="normal">
        <TextField
          id="birthdate"
          label="Fecha de Nacimiento"
          type="date"
          value={birthdate}
          variant="outlined"
          sx={{ width: '650px' }}
          InputLabelProps={{ shrink: true }}
          onChange={(e) => setBirthdate(e.target.value)}
        />
      </FormControl>


      <FormControlLabel
        control={
          <Checkbox
            checked={role}
            onChange={(e) => setRole(e.target.checked)}
            color="primary"
          />
        }
        label="¿Es ejecutivo?"
        sx={{ width: '650px' }}
      />

      <Button
        variant="contained"
        color="primary"
        type="submit"
        sx={{ marginTop: "1rem", width: '650px' }}
      >
        Registrar
      </Button>
    </Box>
  );
};

export default UserRegister;
