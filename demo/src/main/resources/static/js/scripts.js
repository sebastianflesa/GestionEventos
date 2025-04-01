document.getElementById('filtroEventos').addEventListener('input', function () {
    const filtro = this.value.toLowerCase();
    document.querySelectorAll('.evento').forEach(evento => {
        const nombre = evento.getAttribute('data-nombre');
        const desc = evento.getAttribute('data-desc');
        const fecha = evento.getAttribute('data-fecha');
        const likes = evento.getAttribute('data-likes');

        const coincide = nombre.includes(filtro) ||
                        desc.includes(filtro) ||
                        fecha.includes(filtro) ||
                        likes.includes(filtro);

        evento.style.display = coincide ? 'block' : 'none';
    });
});
