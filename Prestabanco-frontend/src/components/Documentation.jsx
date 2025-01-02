import React, { useState } from "react";

export default function CreditInfo() {
  const [activeTab, setActiveTab] = useState("simulate");

  const renderContent = () => {
    switch (activeTab) {
      case "simulate":
        return (
          <div>
            <h2>Simular un Crédito</h2>
            <p>
              Aquí puedes realizar una simulación de un crédito, seleccionando el tipo de credito e ingresando los datos como monto (Capital), tasa de interés anual y plazo.<br />

              El sistema mostrara las condiciones para tomar el credito, solicitado y cuanto seria el pago mensual a realizar.
            </p>
          </div>
        );
      case "request":
        return (
          <div>
            <h2>Solicitar un Crédito</h2>
            <p>
              Para completar la solictud necesita seleccionar el tipo de credito, en base a esto tiene un diferente intervalo aceptable de intereses y plazos.<br />

              Aparte de esto tiene que rellenar el monto, los ingresos, el valor de la propiedad, las deudas que posee y los documentos necesarios para la solicitud.
            </p>
          </div>
        );
      case "evaluate":
        return (
          <div>
            <h2>Evaluar Créditos</h2>
            <p>
              Para evaluar el credito presione evaluar, sobre el credito que desea evaluar, el proceso de evaluacion consiste de 7 pasos.<br />
              <br />
              R1. Relación Cuota/Ingreso
              <br />
              R2. Historial Crediticio del Cliente
              <br />
              R3. Antigüedad Laboral y Estabilidad
              <br />
              R4. Relación Deuda/Ingreso
              <br />
              R5. Monto Máximo de Financiamiento
              <br />
              R6. Edad del Solicitante
              <br />
              R7. Capacidad de Ahorro
              <br />
              De estos pasos estan automatizados el paso 1,4,5,6 el resto de pasos requieren que el ejecutivo analize los documentos y decida si la solicitud es apta para continuar.<br />
              Caso contrario puede rechazar la solicitud.
            </p>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div>
      {/* Tabs or Buttons */}
      <div style={{ display: "flex", justifyContent: "center", marginBottom: "1rem" }}>
        <button
          onClick={() => setActiveTab("simulate")}
          style={{
            padding: "10px 20px",
            margin: "0 10px",
            backgroundColor: activeTab === "simulate" ? "#007BFF" : "#EEE",
            color: activeTab === "simulate" ? "#FFF" : "#000",
            border: "none",
            cursor: "pointer",
          }}
        >
          Simular
        </button>
        <button
          onClick={() => setActiveTab("request")}
          style={{
            padding: "10px 20px",
            margin: "0 10px",
            backgroundColor: activeTab === "request" ? "#007BFF" : "#EEE",
            color: activeTab === "request" ? "#FFF" : "#000",
            border: "none",
            cursor: "pointer",
          }}
        >
          Solicitar
        </button>
        <button
          onClick={() => setActiveTab("evaluate")}
          style={{
            padding: "10px 20px",
            margin: "0 10px",
            backgroundColor: activeTab === "evaluate" ? "#007BFF" : "#EEE",
            color: activeTab === "evaluate" ? "#FFF" : "#000",
            border: "none",
            cursor: "pointer",
          }}
        >
          Evaluar
        </button>
      </div>

      {/* Content */}
      <div style={{ padding: "20px", border: "1px solid #DDD", borderRadius: "5px" }}>
        {renderContent()}
      </div>
    </div>
  );
}
