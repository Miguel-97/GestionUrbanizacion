miForm.confirmar.onclick = validar;

function validar() {
  var expNombre = new RegExp("^[a-zñáéíóúç 1-9ü-]{3,30}$");
  var expHorario = new RegExp("^(0?[0-9]|1[0-9]|2[0-3]):(0?[0-9]|[1-5][0-9])-(0?[0-9]|1[0-9]|2[0-3]):(0?[0-9]|[1-5][0-9])$");
  if (expNombre.test(miForm.nombre.value.toLowerCase().trim())) {
    miForm.datos.value += "Nombre: " + miForm.nombre.value;
  } else {
    console.log("error");
  }
  if (expHorario.test(miForm.horario.value.trim())) {
    miForm.datos.value += " Horario: " + miForm.horario.value;
  } else {
    console.log("error");
  }

}


/* var expNombre = new RegExp("^[a-zñáéíóúç ü-]{3,30}$");
  var expApellido = new RegExp("^[a-zñáéíóúç ü-]{3,40}$");
  var expTelefono = new RegExp("^[(][+][0-9]{2,3}[)][-][6789][0-9]{8}$");
  var expDni = new RegExp("^[0-9]{8}[A-Z]$");
  var expGenero = new RegExp("^[mfo]");
  var expCalle = new RegExp("^[a-zñáéíóú(ç) ü-]{3,25}$");
  var expNumero = new RegExp("^[1-9][0-9]{0,3}$");
  var expPiso = new RegExp("^[1-9][0-9]{0,2}$");
  var expPuerta = new RegExp("^[1-9ABCDEFGHI]$");
  var expCp = new RegExp("^\\d{5}$");


  if (expApellido.test(miForm.apellidos.value.toLowerCase().trim())) {
    miForm.datos.value += "Apellidos y Nombre: " + miForm.apellidos.value;
  } else {
    console.log("error");
  }
  if (expNombre.test(miForm.nombre.value.toLowerCase().trim())) {
    miForm.datos.value += " " + miForm.nombre.value;
  } else {
    console.log("error");
  }
  if (expTelefono.test(miForm.telefono.value.trim())) {
    miForm.datos.value += "Telefono: " + miForm.telefono.value + tipoTel(miForm.telefono.value);
  } else {
    console.log("error");
  }
  if (expDni.test(miForm.dni.value.toUpperCase().trim()) && validarDni()) {
    miForm.datos.value += "Dni: " + miForm.dni.value.toUpperCase();
  } else {
    console.log("errorDni");
  }
  if (expGenero.test(miForm.genero.value.trim())) {
    miForm.datos.value += "Genero: " + miForm.genero.value;
  } else {
    console.log("error");
  }
  if (expCalle.test(miForm.calle.value.toLowerCase().trim())) {
    miForm.datos.value += "Calle:  " + miForm.calle.value;
  } else {
    console.log("error");
  }
  if (expNumero.test(miForm.n.value.trim())) {
    miForm.datos.value += ", N: " + miForm.n.value;
  } else {
    console.log("error");
  }
  if (expPiso.test(miForm.piso.value.trim())) {
    miForm.datos.value += ", Piso: " + miForm.piso.value;
  } else {
    console.log("error");
  }
  if (expPuerta.test(miForm.puerta.value.toUpperCase().trim())) {
    miForm.datos.value += ", Puerta: " + miForm.puerta.value.toUpperCase();
  } else {
    console.log("error");
  }
  if (expCp.test(miForm.cp.value.trim())) {
    miForm.datos.value += ", Codigo Postal: (" + miForm.cp.value + ")";
  } else {
    console.log("error");

  }
}

function select() {
  if (miForm.pais.selectedIndex == 1) {
    miForm.city.innerHTML = ''
    miForm.city.innerHTML += '<option>Madrid</option><option>Barcelona</option><option>Sevilla</option>'
  } else if (miForm.pais.selectedIndex == 2) {
    miForm.city.innerHTML = ''
    miForm.city.innerHTML += '<option>Paris</option><option>Marsella</option><option>Lyon</option>'
  } else if (miForm.pais.selectedIndex == 3) {
    miForm.city.innerHTML = ''
    miForm.city.innerHTML += '<option>Lisboa</option><option>Oporto</option><option>Sintra</option>'
  } else {
    miForm.city.innerHTML = ''
    miForm.city.innerHTML += '<option>Selecciona un pais</option>'

  }
}

function validarDni() {
  letras = ['T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'O', 'V', 'H', 'L', 'C', 'K', 'E'];
  indice = miForm.dni.value.substr(0, 8) % 23;
  letra = miForm.dni.value.substr(8);
  console.log(letra + "--" + letras[indice] + "--" + indice);
  if (letra == letras[indice]) {
    return true;
  }
}

function tipoTel(numero) {
  var tel;
  if (numero.length == 16) {
    tel = numero.substr(7);
  } else {
    tel = numero.substr(6);
  }
  if (tel[0] == "6" || tel[0] == "7") {
    return " Telefono movil";
  } else if (tel[0] == "8" || tel[0] == "9") {
    return " Telefono linea fija";
  }
}
//(+34)-987456321
 */
