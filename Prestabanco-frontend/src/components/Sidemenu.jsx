import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import PaidIcon from "@mui/icons-material/Paid";
import CalculateIcon from "@mui/icons-material/Calculate";
import AnalyticsIcon from "@mui/icons-material/Analytics";
import DiscountIcon from "@mui/icons-material/Discount";
import HailIcon from "@mui/icons-material/Hail";
import MedicationLiquidIcon from "@mui/icons-material/MedicationLiquid";
import MoreTimeIcon from "@mui/icons-material/MoreTime";
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
