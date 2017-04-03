function mostrarContaDestino(value) {
	console.log(value);
	document.getElementById("dvContaDestino").style.display = ((value == 'TRANSFERENCIA') ? 'inline'
			: 'none');
}

function atualizarSaldo(value) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange=function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("saldo").innerHTML = this.responseText;
		}
	};
	xhttp.open("POST", "atualizarSaldo", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("id=" + value);
}