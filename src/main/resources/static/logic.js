const API_BASE = '/api';
    // --- Funciones del Modal ---
    function openModal() {
        document.getElementById('createModal').classList.add('active');
    }

    function closeModal() {
         document.getElementById('createModal').classList.remove('active');
    }
    // --- Carga Inicial ---
    let currentView = 'week'; // 'week' | 'day'
    let currentDate = new Date();
    document.addEventListener('DOMContentLoaded', () => {
        cargarTerapeutas();
        cargarTiposSesion();
        renderCalendar(); // Cargar calendario al inicio
    });
    // --- Funciones del Calendario ---
    function getMonday(d) {
        d = new Date(d);
        var day = d.getDay(),
            diff = d.getDate() - day + (day == 0 ? -6 : 1); // adjust when day is sunday
        return new Date(d.setDate(diff));
    }
    function formatDate(date) {
        return date.toISOString().split('T')[0];
    }
    function renderCalendar() {
        const grid = document.getElementById('calendarGrid');
        grid.innerHTML = '<div class="grid-cell full-width">Cargando...</div>';
        let endpoint = '';
        let params = '';
        if (currentView === 'week') {
            const monday = getMonday(currentDate);
            endpoint = `${API_BASE}/appointments/week`;
            params = `?monday=${formatDate(monday)}`;
        } else {
            endpoint = `${API_BASE}/appointments/day`;
            params = `?date=${formatDate(currentDate)}`;
        }
        fetch(endpoint + params)
            .then(res => res.json())
            .then(appointments => {
                buildGrid(appointments);
            })
            .catch(err => {
                console.error(err);
                grid.innerHTML = '<div class="grid-cell full-width">Error cargando citas</div>';
            });
    }
    function buildGrid(appointments) {
        const grid = document.getElementById('calendarGrid');
        grid.innerHTML = ''; // Limpiar
        // Definir días a mostrar
        let daysToShow = [];
        if (currentView === 'week') {
            const monday = getMonday(currentDate);
            for (let i = 0; i < 5; i++) { // Lunes a Viernes
                let d = new Date(monday);
                d.setDate(monday.getDate() + i);
                daysToShow.push(d);
            }
        } else {
            daysToShow.push(currentDate);
        }
        // 1. Renderizar Encabezados
        // Columna Hora
        const timeHeader = document.createElement('div');
        timeHeader.className = 'grid-header';
        timeHeader.textContent = 'Hora';
        grid.appendChild(timeHeader);
        // Columnas Días
        const diasSemana = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
        daysToShow.forEach(d => {
            const header = document.createElement('div');
            header.className = 'grid-header';
            header.innerHTML = `${diasSemana[d.getDay()]} <br> <span class="badge badge-green">${d.getDate()}</span>`;
            grid.appendChild(header);
        });
        // 2. Renderizar Filas de Horas (09:00 a 18:00)
        for (let hour = 9; hour <= 18; hour++) {
            // Celda de Hora
            const timeCell = document.createElement('div');
            timeCell.className = 'grid-cell time-col';
            timeCell.textContent = `${hour}:00`;
            grid.appendChild(timeCell);
            // Celdas para cada día
            daysToShow.forEach(dayDate => {
                const cell = document.createElement('div');
                cell.className = 'grid-cell';                    
                // Buscar cita para este día y hora
                // Nota: Esto es una simplificación. En producción idealmente se valida rango de minutos.
                const appt = appointments.find(a => {
                    const aDate = new Date(a.start);
                    return aDate.getDate() === dayDate.getDate() && 
                            aDate.getMonth() === dayDate.getMonth() &&
                           aDate.getHours() === hour;
                });
                if (appt) {
                    const card = document.createElement('div');
                    card.className = 'appointment-card';
                    // Estilo diferente según tipo
                    if (appt.sessionType === 'CITA_TERAPEUTICA') {
                        card.style.backgroundColor = '#bfdbfe';
                        card.style.borderColor = '#93c5fd';
                        card.style.color = '#1e40af';
                    }
                    card.innerHTML = `<strong>${appt.patientNombre}</strong><br>${appt.therapistNombre}<strong><br>${appt.roomId}</strong><strong><br>${appt.roomId}</strong>`;
                    cell.appendChild(card);
                }
                grid.appendChild(cell);
            });
        }
    }
    function switchView(view) {
        currentView = view;
        // Actualizar estilos de botones (simple)
        const btnLeft = document.querySelector('.btn-left');
        const btnRight = document.querySelector('.btn-right');
        const btnMiddle = document.querySelector('btn-middle');
        if (view === 'week') {     
            btnLeft.style.backgroundColor = 'var(--blue-600)';
            btnLeft.style.color = 'white'; 
            btnRight.style.backgroundColor = '#e5e7eb';
            btnRight.style.color = 'var(--text-gray-800)';     
            // Ajustar grid CSS para semana (6 columnas)
            document.getElementById('calendarGrid').style.gridTemplateColumns = 'repeat(6, 1fr)';
        } else if (view === 'day') {
            btnRight.style.backgroundColor = 'var(--blue-600)';
            btnRight.style.color = 'white';
            btnLeft.style.backgroundColor = '#e5e7eb';
            btnLeft.style.color = 'var(--text-gray-800)';    
            // Ajustar grid CSS para día (2 columnas: Hora + Día)
            document.getElementById('calendarGrid').style.gridTemplateColumns = 'repeat(2, 1fr)';
        } 
        renderCalendar();
    }
    function cargarTerapeutas() {
        fetch(`${API_BASE}/catalogs/therapists`)
            .then(response => response.json())
            .then(data => {
                const select = document.getElementById('therapistSelect');
                select.innerHTML = '<option value="">-- Seleccione un Terapeuta --</option>';
                data.forEach(t => {
                    const option = document.createElement('option');
                    option.value = t.id; 
                    // Usamos t.name porque así se llama en la clase Java
                    option.textContent = t.name; 
                    select.appendChild(option);
                });
            })
            .catch(err => console.error("Error cargando terapeutas:", err));
    }
    function cargarTiposSesion() {
        fetch(`${API_BASE}/catalogs/session-types`)
            .then(response => response.json())
            .then(data => {
                const select = document.getElementById('sessionTypeSelect');
                select.innerHTML = '<option value="">-- Seleccione Tipo --</option>';
                data.forEach(tipo => {
                    const option = document.createElement('option');
                    option.value = tipo; // El valor es el string (ej: "CITA_DE_TERAPIA")
                    option.textContent = tipo.replace(/_/g, ' '); // Muestra "CITA DE TERAPIA"
                    select.appendChild(option);
                });
            })
            .catch(err => console.error("Error cargando tipos:", err));
    }
        // --- Enviar Formulario ---
        document.getElementById('citaForm').addEventListener('submit', function(e) {
        e.preventDefault(); 

            // Construimos el objeto tal cual lo espera el Backend
            const sessionTypeValue = document.getElementById('sessionTypeSelect').value;
            if (!sessionTypeValue) {
                alert("Seleccione el tipo de sesión.");
                return;
            }

            const appointmentData = {
                date: document.getElementById('fecha').value,
                startTime: document.getElementById('hora').value + ":00",
                therapistId: parseInt(document.getElementById('therapistSelect').value),
                roomId: 1,
                // patientId: 1, // YA NO USAMOS EL ID FIJO
                patientNombre: document.getElementById('nombre').value,
                patientApellido: document.getElementById('apellidos').value,
                patientTelefono: document.getElementById('telefono').value,
                patientEmail: document.getElementById('correo').value,
                sessionType: sessionTypeValue, 
                comments: `
                    Tipo: ${sessionTypeValue}. 
                    Nota: ${document.getElementById('comentarios').value}
                `
            };

            console.log("Enviando:", appointmentData);

            fetch(`${API_BASE}/appointments`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(appointmentData)
            })
            .then(async response => {
                if (response.ok) {
                    const json = await response.json();
                    alert(`¡Cita agendada con éxito! ID: ${json.appointmentId}`);
                    document.getElementById('citaForm').reset();
                    closeModal();
                } else {
                    const errorText = await response.text();
                 alert(`Error del servidor: ${errorText}`);
            }
     })
        .catch(error => {
        console.error('Error de red:', error);
        alert("Error de conexión: No se pudo contactar al servidor.");
    });
});