document.addEventListener("keyup", e=>{

  if (e.target.matches("#buscador")){

      if (e.key ==="Enter")e.target.value = ""

      document.querySelectorAll(".articulo").forEach(fruta =>{

          fruta.textContent.toLowerCase().includes(e.target.value.toLowerCase())
            ?fruta.classList.remove("filtro")
            :fruta.classList.add("filtro")
      })

  }
})
const $nombre = document.querySelector("#buscador");

// Escuchamos el keydown y prevenimos el evento
$nombre.addEventListener("keydown", (evento) => {
	if (evento.key == "Enter") {
		// Prevenir
		evento.preventDefault();
		return false;
	}
});