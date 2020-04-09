miForm.confirmar.onclick = validar;

function validar() {
  miForm.datos.value = "";
  var expPortal = new RegExp("^(([1-9][0-9]{0,3}[ ]?[a-z]?)|([a-z]{1,3})|([a-z]{2,30}))$");
  var expPisos = new RegExp("^[1-9][0-9]{0,30}$");
  var expPuertasXPiso = new RegExp("^[1-9][0-9]{0,30}$");
  miForm.datos.value += "Estos son los datos a crear... Urbanizacion: " + miForm.urbanizacion.value;
  if (expPortal.test(miForm.portal.value.toLowerCase().trim())) {
    miForm.datos.value += " Portal: " + miForm.portal.value.toUpperCase();
  } else {
    console.log("error");
  }
  if (expPisos.test(miForm.pisos.value.trim())) {
    miForm.datos.value += " Pisos: " + miForm.pisos.value;
  } else {
    console.log("error");
  }
  if (expPuertasXPiso.test(miForm.puertasXPiso.value.trim())) {
    miForm.datos.value += " Puertas por piso : " + miForm.puertasXPiso.value;
  } else {
    console.log("error");
  }

}