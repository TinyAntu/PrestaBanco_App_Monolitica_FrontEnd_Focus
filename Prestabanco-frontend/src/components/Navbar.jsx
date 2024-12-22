import * as React from "react";
import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Sidemenu from "./Sidemenu";

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [isLog, setIsLog] = useState(false);
  const navigate = useNavigate();
  const location = useLocation(); // To detect route changes

  const toggleDrawer = (open) => (event) => {
    setOpen(open);
  };

  // Check login status on every route change
  useEffect(() => {
    const userId = localStorage.getItem("userId");
    setIsLog(!!userId); // Update `isLog` if userId exists
  }, [location]); // Dependency on `location` to re-check on route change

  const handleLogout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("userTypeId");
    setIsLog(false);
    navigate("/user/login");
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>

          {!isLog && (
            <>
              <Button color="inherit" onClick={() => navigate("/user/register")}>
                Register
              </Button>
              <Button color="inherit" onClick={() => navigate("/user/login")}>
                Login
              </Button>
            </>
          )}

          {isLog && (
            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          )}

          <Typography variant="h6" component="div" sx={{ flexGrow: 1, textAlign: "center" }}>
            PrestaBanco: Sistema de Créditos Bancarios
          </Typography>
        </Toolbar>
      </AppBar>

      <Sidemenu open={open} toggleDrawer={toggleDrawer}></Sidemenu>
    </Box>
  );
}

