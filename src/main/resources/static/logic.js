
    //LÓGICA JAVASCRIPT (Conectada al Backend)
        const API_BASE = '/api';

        // --- Funciones del Modal ---//hecho por alexander
        function openModal() {
            const hoy = new Date().toISOString().split('T')[0];// se añade logica para evitar fechas pasadas
            document.getElementById('fecha').min = hoy;
            document.getElementById('createModal').classList.add('active');
        }

        function closeModal() {
            document.getElementById('createModal').classList.remove('active');
        }

        function showMessage(message, title = "Información") {
            document.getElementById('messageTitle').textContent = title;
            document.getElementById('messageBody').textContent = message;
            document.getElementById('messageBody').style.whiteSpace = 'pre-wrap';
            document.getElementById('messageModal').classList.add('active');
        }

        function closeMessageModal() {
            document.getElementById('messageModal').classList.remove('active');
        }

        // --- Lógica de Confirmación ---
        let onConfirmAction = null;

        function showConfirm(message, onConfirm, title = "Confirmación") {
            document.getElementById('confirmTitle').textContent = title;
            document.getElementById('confirmBody').textContent = message;
            document.getElementById('confirmModal').classList.add('active');
            
            // Guardamos la acción a ejecutar
            onConfirmAction = onConfirm;
        }

        function closeConfirmModal() {
            document.getElementById('confirmModal').classList.remove('active');
            onConfirmAction = null;
        }

        // Asignar evento al botón de confirmar una sola vez
        document.getElementById('confirmBtnAction').addEventListener('click', function() {
            if (onConfirmAction) {
                onConfirmAction();
            }
            closeConfirmModal();
        });

        // --- Carga Inicial ---
        let currentView = 'week'; // 'week' | 'day'
        let currentDate = new Date();
        let allPatients = [];

        document.addEventListener('DOMContentLoaded', () => {
            cargarTerapeutas();
            cargarTiposSesion();
            cargarPacientes();
            cargarSalas();
            updateDateLabel();
            renderCalendar(); // Cargar calendario al inicio
        });

        function cargarSalas() {
            fetch(`${API_BASE}/catalogs/rooms`)
                .then(response => response.json())
                .then(data => {
                    const select = document.getElementById('salaSelect');
                    select.innerHTML = '<option value="">-- Seleccione Sala --</option>';
                    data.forEach(r => {
                        const option = document.createElement('option');
                        option.value = r.id;
                        option.textContent = r.nombre;
                        select.appendChild(option);
                    });

                    const resSelect = document.getElementById('rescheduleRoomSelect');
                    if (resSelect) {
                        resSelect.innerHTML = '<option value="">-- Seleccione Sala --</option>';
                        data.forEach(r => {
                            const opt = document.createElement('option');
                            opt.value = r.id;
                            opt.textContent = r.nombre;
                            resSelect.appendChild(opt);
                        });
                    }
                })
                .catch(err => console.error("Error cargando salas:", err));
        }

        function cargarPacientes() {
             fetch(`${API_BASE}/catalogs/patients`)
                .then(response => response.json())
                .then(data => {
                    allPatients = data;
                    const select = document.getElementById('patientSelect');
                    select.innerHTML = '<option value="">Seleccionar paciente</option>';
                    data.forEach(p => {
                        const option = document.createElement('option');
                        option.value = p.id;
                        option.textContent = `${p.firstName} ${p.lastName}`;
                        select.appendChild(option);
                    });
                })
                .catch(err => console.error("Error cargando pacientes:", err));
        }

        function toggleFormFields() {
            const type = document.getElementById('sessionTypeSelect').value;
            const newPatientFields = document.getElementById('newPatientFields');
            const existingPatientFields = document.getElementById('existingPatientFields');
            const therapyFields = document.getElementById('therapyFields');
            const paymentFields = document.getElementById('paymentFields');

            // Reset displays
            newPatientFields.style.display = 'none';
            existingPatientFields.style.display = 'none';
            therapyFields.style.display = 'none';
            paymentFields.style.display = 'none';

            if (type === 'EVALUACION_INICIAL') {
                newPatientFields.style.display = 'grid';
            } else if (type === 'CITA_DE_TERAPIA') {
                existingPatientFields.style.display = 'grid';
                therapyFields.style.display = 'grid';
                paymentFields.style.display = 'grid';
            } else {
                // Default behavior for other types
                if (type) {
                     existingPatientFields.style.display = 'grid';
                }
            }
        }

        function updateFolio() {
            const patientId = document.getElementById('patientSelect').value;
            const folioInput = document.getElementById('folio');
            
            if (patientId) {
                const patient = allPatients.find(p => p.id == patientId);
                if (patient) {
                    folioInput.value = patient.folio || '';
                }
            } else {
                folioInput.value = '';
            }
        }

        function changeDate(offset) {
            if (currentView === 'week') {
                currentDate.setDate(currentDate.getDate() + (offset * 7));
            } else {
                currentDate.setDate(currentDate.getDate() + offset);
            }
            updateDateLabel();
            renderCalendar();
        }

        function updateDateLabel() {
            const label = document.getElementById('currentDateLabel');
            if (currentView === 'week') {
                const monday = getMonday(currentDate);
                const friday = new Date(monday);
                friday.setDate(monday.getDate() + 4);
                label.textContent = `${monday.toLocaleDateString()} - ${friday.toLocaleDateString()}`;
            } else {
                label.textContent = currentDate.toLocaleDateString();
            }
        }

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
        function formatDateLocal(date){
            const y = date.getFullYear();
            const m = String(date.getMonth()+1).padStart(2,"0");
            const d = String(date.getDate()).padStart(2,"0");
            return `${y}-${m}-${d}`;

        }

        function renderCalendar() {
            const grid = document.getElementById('calendarGrid');
            grid.innerHTML = '<div class="grid-cell full-width">Cargando...</div>';

            let endpoint = '';
            let params = '';

            if (currentView === 'week') {
                const monday = getMonday(currentDate);
                endpoint = `${API_BASE}/appointments/week`;
                params = `?monday=${formatDateLocal(monday)}`;
            } else {
                endpoint = `${API_BASE}/appointments/day`;
                params = `?date=${formatDateLocal(currentDate)}`;
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

        // --- Funciones del Modal de Detalles ---
        function openDetailsModal(appt) {
            // Guardar referencia global para acciones posteriores (reprogramar)
            window.currentDetailAppt = appt;
            document.getElementById('detailType').textContent = appt.sessionType;
            document.getElementById('detailPatient').textContent = appt.patientNombre;
            document.getElementById('detailFolio').textContent = appt.patientFolio || '---';
            
            const start = new Date(appt.start);
            const end = new Date(appt.end);
            const duration = (end - start) / (1000 * 60); // minutos

            document.getElementById('detailDate').textContent = start.toLocaleDateString();
            document.getElementById('detailStart').textContent = start.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            document.getElementById('detailDuration').textContent = duration + ' min';
            document.getElementById('detailRoom').textContent = appt.roomNombre || '---';
            document.getElementById('detailTherapist').textContent = appt.therapistNombre;
            
            // Normalizar estado a mayúsculas para comparación
            const status = appt.status ? appt.status.toUpperCase() : 'PENDIENTE';
            document.getElementById('detailStatus').textContent = status;

            const btnCancel = document.getElementById('btnCancelAppt');
            if (status === 'CANCELADO') {
                btnCancel.style.display = 'none';
            } else {
                btnCancel.style.display = 'inline-block';
                btnCancel.onclick = () => cancelAppointment(appt.id);
            }

            // Botón reprogramar
            const btnRescheduleOpen = document.getElementById('btnRescheduleOpen');
            if (btnRescheduleOpen) {
                btnRescheduleOpen.onclick = () => openRescheduleModal(appt);
                if (status === 'CANCELADO') {
                    btnRescheduleOpen.style.display = 'none';
                } else {
                    btnRescheduleOpen.style.display = 'inline-block';
                }
            }

            document.getElementById('detailsModal').classList.add('active');
        }

        function closeDetailsModal() {
            document.getElementById('detailsModal').classList.remove('active');
        }

        function cancelAppointment(id) {
            showConfirm('¿Está seguro de que desea cancelar esta cita? Esta acción no se puede deshacer.', () => {
                fetch(`${API_BASE}/appointments/${id}/cancel`, {
                    method: 'POST'
                })
                .then(async res => {
                    if (res.ok) {
                        showMessage('Cita cancelada correctamente.', 'Éxito');
                        closeDetailsModal();
                        renderCalendar();
                    } else {
                        const txt = await res.text();
                        showMessage('Error al cancelar: ' + txt, 'Error');
                    }
                })
                .catch(err => {
                    console.error(err);
                    showMessage('Error de conexión.', 'Error');
                });
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
                    
                    // Buscar TODAS las citas para este día y hora
                    const cellAppointments = appointments.filter(a => {
                        const aDate = new Date(a.start);
                        return aDate.getDate() === dayDate.getDate() && 
                               aDate.getMonth() === dayDate.getMonth() &&
                               aDate.getHours() === hour;
                    });

                    // Renderizar cada cita encontrada
                    cellAppointments.forEach(appt => {
                        const card = document.createElement('div');
                        card.className = 'appointment-card';
                        
                        // Normalizar estado a mayúsculas
                        const status = appt.status ? appt.status.toUpperCase() : 'PENDIENTE';

                        if (status === 'CANCELADO') {
                            card.classList.add('cancelled');
                        } else if (appt.sessionType === 'CITA_TERAPEUTICA') {
                             card.style.backgroundColor = '#bfdbfe';
                             card.style.borderColor = '#93c5fd';
                             card.style.color = '#1e40af';
                        }
                        
                        card.innerHTML = `<strong>${appt.patientNombre}</strong><br>${appt.therapistNombre}`;
                        card.onclick = () => openDetailsModal(appt);
                        cell.appendChild(card);
                    });

                    grid.appendChild(cell);
                });
            }
        }

        function switchView(view) {
            currentView = view;
            // Actualizar estilos de botones (simple)
            const btnLeft = document.querySelector('.btn-left');
            const btnRight = document.querySelector('.btn-right');
            if (view === 'week') {
                btnLeft.style.backgroundColor = 'var(--blue-600)';
                btnLeft.style.color = 'white';
                btnRight.style.backgroundColor = '#e5e7eb';
                btnRight.style.color = 'var(--text-gray-800)';
                // Ajustar grid CSS para semana (6 columnas)
                document.getElementById('calendarGrid').style.gridTemplateColumns = 'repeat(6, 1fr)';
            } else {
                btnRight.style.backgroundColor = 'var(--blue-600)';
                btnRight.style.color = 'white';
                btnLeft.style.backgroundColor = '#e5e7eb';
                btnLeft.style.color = 'var(--text-gray-800)';
                // Ajustar grid CSS para día (2 columnas: Hora + Día)
                document.getElementById('calendarGrid').style.gridTemplateColumns = 'repeat(2, 1fr)';
            }
            updateDateLabel();
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

                    // También popular select del modal de reprogramar si existe
                    const resSelect = document.getElementById('rescheduleTherapistSelect');
                    if (resSelect) {
                        resSelect.innerHTML = '<option value="">-- Seleccione un Terapeuta --</option>';
                        data.forEach(t => {
                            const opt = document.createElement('option');
                            opt.value = t.id;
                            opt.textContent = t.name;
                            resSelect.appendChild(opt);
                        });
                    }
                })
                .catch(err => console.error("Error cargando terapeutas:", err));
        }

        // --- Reprogramar / Actualizar Cita ---
        function openRescheduleModal(appt) {
            // Prefill values
            const hoy = new Date().toISOString().split('T')[0];// se añade logica para evitar fechas pasadas(hecho por rolando)
            document.getElementById('rescheduleDate').min = hoy;
            const start = new Date(appt.start);
            document.getElementById('rescheduleDate').value = start.toISOString().split('T')[0];
            document.getElementById('rescheduleTime').value = start.toTimeString().slice(0,5);
            const end = new Date(appt.end);
            const duration = (end - start) / (1000*60);
            document.getElementById('rescheduleDuration').value = duration;
            const resSel = document.getElementById('rescheduleTherapistSelect');
            if (resSel) {
                // intentar seleccionar terapeuta por id si está disponible
                for (let i=0;i<resSel.options.length;i++){
                    if (appt.therapistId && resSel.options[i].value == appt.therapistId) {
                        resSel.selectedIndex = i; break;
                    }
                    if (resSel.options[i].textContent == appt.therapistNombre) {
                        resSel.selectedIndex = i; break;
                    }
                }
            }

            const resRoomSel = document.getElementById('rescheduleRoomSelect');
            if (resRoomSel) {
                for (let i=0;i<resRoomSel.options.length;i++){
                    if (appt.roomId && resRoomSel.options[i].value == appt.roomId) {
                        resRoomSel.selectedIndex = i; break;
                    }
                    if (resRoomSel.options[i].textContent == appt.roomNombre) {
                        resRoomSel.selectedIndex = i; break;
                    }
                }
            }
            document.getElementById('rescheduleComments').value = appt.comments || '';
            document.getElementById('rescheduleModal').classList.add('active');
        }

        function closeRescheduleModal() {
            document.getElementById('rescheduleModal').classList.remove('active');
        }

        document.getElementById('rescheduleSaveBtn').addEventListener('click', function() {
            const appt = window.currentDetailAppt;
            if (!appt) {
                showMessage('No se encontró la cita seleccionada.', 'Error');
                return;
            }
            const payload = {
                date: document.getElementById('rescheduleDate').value,
                startTime: document.getElementById('rescheduleTime').value + ':00',
                durationMinutes: parseInt(document.getElementById('rescheduleDuration').value) || 60,
                therapistId: parseInt(document.getElementById('rescheduleTherapistSelect').value),
                roomId: parseInt(document.getElementById('rescheduleRoomSelect').value),
                comments: document.getElementById('rescheduleComments').value
            };

            if (!payload.date || !payload.startTime || !payload.therapistId) {
                showMessage('Faltan datos obligatorios: Fecha, Hora o Terapeuta.', 'Atención');
                return;
            }

            fetch(`${API_BASE}/appointments/${appt.id}/reschedule`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(async res => {
                if (res.ok) {
                    const json = await res.json();
                    showMessage('Cita actualizada correctamente.', 'Éxito');
                    // cerrar modales: reprogramar y detalles
                    closeRescheduleModal();
                    closeDetailsModal();
                    renderCalendar();
                } else {
                    const txt = await res.text();
                    showMessage('Error al actualizar: ' + txt, 'Error');
                }
            })
            .catch(err => {
                console.error(err);
                showMessage('Error de conexión.', 'Error');
            });
        });

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
                showMessage("Seleccione el tipo de sesión.", "Atención");
                return;
            }

            let appointmentData = {
                date: document.getElementById('fecha').value,
                startTime: document.getElementById('hora').value + ":00",
                therapistId: parseInt(document.getElementById('therapistSelect').value),
                roomId: parseInt(document.getElementById('salaSelect').value) || 1,
                sessionType: sessionTypeValue, 
                comments: document.getElementById('comentarios').value
            };

            // Validaciones Generales
            if (!appointmentData.date || !document.getElementById('hora').value || !appointmentData.therapistId || !appointmentData.roomId) {
                showMessage("Faltan datos obligatorios: Fecha, Hora, Terapeuta o Sala.", "Atención");
                return;
            }

            if (sessionTypeValue === 'EVALUACION_INICIAL') {
                appointmentData.patientNombre = document.getElementById('nombre').value;
                appointmentData.patientApellido = document.getElementById('apellidos').value;
                appointmentData.patientTelefono = document.getElementById('telefono').value;
                appointmentData.patientEmail = document.getElementById('correo').value;

                if (!appointmentData.patientNombre || !appointmentData.patientApellido || !appointmentData.patientTelefono || !appointmentData.patientEmail) {
                    showMessage("Faltan datos del paciente (Nombre, Apellido, Teléfono, Email).", "Atención");
                    return;
                }

            } else {
                // CITA_DE_TERAPIA or others
                const pId = document.getElementById('patientSelect').value;
                if (!pId) {
                    showMessage("Seleccione un paciente.", "Atención");
                    return;
                }
                appointmentData.patientId = parseInt(pId);
                appointmentData.folio = document.getElementById('folio').value;
                
                const cuota = document.getElementById('cuota').value;
                if (cuota) appointmentData.amountMx = parseFloat(cuota);
                
                // Validación de Comprobante de Pago
                const fileInput = document.getElementById('comprobante');
               // if (sessionTypeValue === 'CITA_DE_TERAPIA') {
                    //if (fileInput.files.length === 0) {
                   //     showMessage("Falta documentación: Debe subir el comprobante de pago.", "Atención");
                 //       return;
                    //}
              //  }

                if (fileInput.files.length > 0) {
                    appointmentData.paymentProof = fileInput.files[0].name;
                }
            }

            console.log("Enviando:", appointmentData);

            fetch(`${API_BASE}/appointments`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(appointmentData)
            })
            .then(async response => {
                if (response.ok) {
                    const json = await response.json();
                    showMessage(`¡Cita agendada con éxito!`, "Éxito");
                    document.getElementById('citaForm').reset();
                    closeModal();
                    toggleFormFields(); // Reset visibility
                } else {
                    const errorText = await response.text();
                    // Intentar parsear si es JSON para mostrar mensaje limpio
                    try {
                        const errObj = JSON.parse(errorText);
                        
                        // Si hay un mensaje explícito (ej: RuntimeException message)
                        if (errObj.message && errObj.message !== 'No message available') {
                            showMessage(errObj.message, "Error");
                        } 
                        // Si es un mapa de errores (fallback)
                        else if (typeof errObj === 'object') {
                            let msg = "";
                            for (const key in errObj) {
                                // Filtrar campos técnicos para no ensuciar el mensaje
                                if (key !== 'trace' && key !== 'timestamp' && key !== 'path' && key !== 'status' && key !== 'error') {
                                    msg += `- ${key}: ${errObj[key]}\n`;
                                }
                            }
                            // Si quedó vacío, usar el error genérico
                            if (!msg && errObj.error) msg = errObj.error;
                            
                            showMessage(msg || "Ocurrió un error inesperado.", "Error");
                        } else {
                            showMessage(`Error: ${errorText}`, "Error");
                        }
                    } catch (e) {
                        // Si es texto plano
                        showMessage(`Error: ${errorText}`, "Error");
                    }
                }
            })
            .catch(error => {
                console.error('Error de red:', error);
                showMessage("Error de conexión: No se pudo contactar al servidor.", "Error de Conexión");
            });
        });
        //Formulario -> Input-Date -> Logica con javascript para la seleccion de dias...
        flatpickr("#fecha",{
            dateFormat: "Y-m-d",
            disable : [
                function(date){
                    const day = date.getDay();
                    return (day === 0 || day === 6)
                }
            ],
            minDate: "today",
            locale: "es"
            
        })
        //Formulario -> forms-data -> logica para resetear a valores por defecto al cerrar el formulario
        const closeBtn = document.getElementById("close-btn-forms");
        const formsData = document.getElementById("citaForm");

        closeBtn.addEventListener("click", () => {
            formsData.reset();
        })