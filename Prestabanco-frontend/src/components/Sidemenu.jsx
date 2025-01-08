import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import { QuestionMark } from "@mui/icons-material";
import CalculateIcon from "@mui/icons-material/Calculate";
import PaidIcon from "@mui/icons-material/Paid";
import HomeIcon from "@mui/icons-material/Home";
import { useNavigate } from "react-router-dom";

export default function Sidemenu({ open, toggleDrawer }) {
  const navigate = useNavigate();

  const isLog = () => {
    return !!localStorage.getItem("userId");
  };

  const handleNavigation = (path) => {
    if (isLog() || path === "/home") {
      navigate(path);
    } else {
      alert("Please login to enter this section.");
      navigate("/user/login");
    }
  };

  React.useEffect(() => {
    const handleKeydown = (event) => {

      // Atajo para "Home" (Alt+0)
      if (event.altKey && event.key === "0") {
        handleNavigation("/home");
      }

      // Atajo para "Simular un Crédito" (Alt+1)
      if (event.altKey && event.key === "1") {
        handleNavigation("/credits/simulate");
      }

      // Atajo para "Solicitar Crédito" (Alt+2)
      if (event.altKey && event.key === "2") {
        if (isLog()) {
          const userId = localStorage.getItem("userId");
          navigate(`/credits/create/${userId}`);
        } else {
          alert("Please log in to enter.");
          navigate("/user/login");
        }
      }

      // Atajo para "Seguimiento de solicitud" (Alt+3)
      if (event.altKey && event.key === "3") {
        handleNavigation("/credits/follow");
      }

      // Atajo para "Evaluar Créditos" (Alt+4)
      if (event.altKey && event.key === "4") {
        handleNavigation("/credits/getAll");
      }

      // Atajo para "Documentacion" (Ctrl+9)
      if (event.altKey && event.key === "9") {
        handleNavigation("/help");
      }

    };

    // Añadir listener de teclado
    window.addEventListener("keydown", handleKeydown);

    return () => {
      // Limpiar listener al desmontar el componente
      window.removeEventListener("keydown", handleKeydown);
    };
  }, [navigate]); // Dependencia de `navigate`



  const listOptions = () => (
    <Box
      role="presentation"
      onClick={toggleDrawer(false)}
    >
      <List>
        <ListItemButton onClick={() => handleNavigation("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <Divider />

        <ListItemButton onClick={() => handleNavigation("/credits/simulate")}>
          <ListItemIcon>
            <CalculateIcon />
          </ListItemIcon>
          <ListItemText primary="Simular un Credito" />
        </ListItemButton>

        <ListItemButton
          onClick={() => {
            if (isLog()) {
              const userId = localStorage.getItem("userId");
              navigate(`/credits/create/${userId}`);
            } else {
              alert("Please log in to enter.");
              navigate("/user/login");
            }
          }}
        >
          <ListItemIcon>
            <CalculateIcon />
          </ListItemIcon>
          <ListItemText primary="Solicitar Crédito" />
        </ListItemButton>

        <ListItemButton onClick={() => handleNavigation("/credits/follow")}>
          <ListItemIcon>
            <PaidIcon />
          </ListItemIcon>
          <ListItemText primary="Seguimiento de solicitud" />
        </ListItemButton>
  
      </List>

      <Divider />

      <List>
        
      <ListItemButton onClick={() => handleNavigation("/credits/getAll")}>
          <ListItemIcon>
            <CalculateIcon />
          </ListItemIcon>
          <ListItemText primary="Evaluar creditos" />
        </ListItemButton>

        <ListItemButton onClick={() => handleNavigation("/help")}>
          <ListItemIcon>
            <QuestionMark />
          </ListItemIcon>
          <ListItemText primary="Ayuda y Documentacion" />
        </ListItemButton>
        
      </List>
    </Box>
  );

  return (
    <div>
      <Drawer anchor={"left"} open={open} onClose={toggleDrawer(false)}>
        {listOptions()}
      </Drawer>
    </div>
  );
}
