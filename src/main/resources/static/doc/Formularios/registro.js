miForm.confirmar.onclick = validar;

function validar() {
  miForm.datos.value = "";
  var expDomicilioU = new RegExp("^([0-9a-zñáéíóúç ü-]{3,30})$");
  var expDomicilioPo = new RegExp("^(([1-9][0-9]{0,3}[ ]?[a-z]?)|([a-z]{0,3}))$");
  var expDomicilioPi = new RegExp("^([1-9][0-9]{0,3})$");
  var expDomicilioPu = new RegExp("^([1-9][0-9]{0,3}|[a-z])$");
  var expNombre = new RegExp("^[a-zñáéíóúç ü-]{3,30}$");
  var expEmail = new RegExp("^[a-z0-9ñç](\.?[a-z_0-9-ñç]){2,40}@[a-zñç]{2,20}.[a-zñç]{2,5}$");
  if (expDomicilioU.test(miForm.domicilioU.value.toLowerCase().trim()) && expDomicilioPo.test(miForm.domicilioPo.value.toLowerCase().trim()) && expDomicilioPi.test(miForm.domicilioPi.value.trim()) && expDomicilioPu.test(miForm.domicilioPu.value.toLowerCase().trim())) {
    var domicilioCompleto = "Urbanizacion: " + miForm.domicilioU.value + " Portal: " + miForm.domicilioPo.value + " Piso: " + miForm.domicilioPi.value + " Puerta: " + miForm.domicilioPu.value;
    miForm.datos.value += "Domicilio Completo: " + domicilioCompleto;
  } else {
    console.log("error");
  }
  if (expNombre.test(miForm.nombre.value.toLowerCase().trim())) {
    miForm.datos.value += " Nombre: " + miForm.nombre.value;
  } else {
    console.log("error");
  }
  if (expEmail.test(miForm.email.value.trim())) {
    miForm.datos.value += " Email: " + miForm.email.value;
  } else {
    console.log("error");
  }
}

