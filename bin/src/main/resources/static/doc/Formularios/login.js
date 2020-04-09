miForm.confirmar.onclick = validar;


function validarContra() {
  var contra = miForm.contra.value.trim();
  var validos1 = "0123456789";
  var validos2 = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
  var validos3 = "abcdefghijklmnñopqrstuvwxyz";
  var criterio1 = false;
  var criterio2 = false;
  var criterio3 = false;
  var ok = false;
  for (var i = 0; i < contra.length; i++) {
    if (validos1.indexOf(contra.charAt(i)) >= 0) {
      criterio1 = true;
    }
  }
  for (var i = 0; i < contra.length; i++) {
    if (validos2.indexOf(contra.charAt(i)) >= 0) {
      criterio2 = true;
    }
  }
  for (var i = 0; i < contra.length; i++) {
    if (validos3.indexOf(contra.charAt(i)) >= 0) {
      criterio3 = true;
    }
  }
  if (criterio1 && criterio2 && criterio3) {
    ok = true;
  }
  return ok;
}

function validar() {
  miForm.datos.value = "";
  var expUsername = new RegExp("^[0-9a-z_ñáéíóúç \.ü-]{3,30}$");
  var expContra = new RegExp("^[a-zA-Z0-9ñç]([\.\$\@]?[a-zA-Z0-9_-ñç]){8,30}$");
  if (expUsername.test(miForm.username.value.toLowerCase().trim())) {
    miForm.datos.value += "Nombre: " + miForm.username.value;
  } else {
    console.log("error");
  }
  if (expContra.test(miForm.contra.value.trim()) && validarContra()) {
    miForm.datos.value += " Contraseña: " + miForm.contra.value;
  } else {
    console.log("error");
  }
}