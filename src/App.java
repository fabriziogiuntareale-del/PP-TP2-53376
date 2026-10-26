import certificacion.Certificable;
import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        ejercicio1();
        ejercicio2();
        ejercicio3();
        ejercicio4();
    }

    private static void ejercicio1() {
        System.out.println(Utilidades.BLUE + " TP2 - EJERCICIO 1 " + Utilidades.RESET);

        System.out.println(Utilidades.BLUE + "\n--- Creación de estudiantes, sala y evento ---" + Utilidades.RESET);
        Estudiante Blas = new Estudiante("1001", "Blas Giunta");
        Estudiante Martin = new Estudiante("1002", "Martin Palermo");
        Estudiante Carlos = new Estudiante("1003", "Carlos Tevez");

        Sala sala = new Sala(1, "Auditorio Principal");

        EventoUniversitario evento = new EventoUniversitario("EVT-001", "Semana de la Ingeniería", 1000.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Introducción a IA", 2, "Charla", "Lic. Riquelme", false);
        evento.crearActividad("Taller de Robótica", 15, "Taller", null, true);
        evento.mostrarDatos();

        Actividad charla = evento.getActividades().get(0);

        System.out.println(Utilidades.BLUE + "\n--- Caso exitoso: inscripciones dentro del cupo ---" + Utilidades.RESET);
        try {
            charla.inscribir(Blas);
            charla.inscribir(Martin);
            System.out.println(Utilidades.GREEN + "Blas y Martin inscriptos correctamente." + Utilidades.RESET);
            boolean guardadoOk = evento.persistirEvento();
            System.out.println("Evento guardado: " + guardadoOk);
            EventoUniversitario recuperado = evento.recuperarEvento(evento.getEventoId());
            System.out.println("Evento recuperado: " + recuperado);
        } catch (CupoExcedidoException e) {
            System.out.println(Utilidades.RED + e.getMessage() + Utilidades.RESET);
        } finally {
            System.out.println("-------------------------");
            System.out.println(Utilidades.YELLOW + "Realizando limpieza..." + Utilidades.RESET);
            File eventoArchivo = new File(evento.getEventoId() + ".dat");
            eventoArchivo.delete();
            System.out.println(Utilidades.GREEN + "Limpieza realizada con éxito" + Utilidades.RESET);
        }

        System.out.println(Utilidades.BLUE + "\n--- Caso fallido: cupo excedido ---" + Utilidades.RESET);
        try {
            charla.inscribir(Carlos);
            System.out.println(Utilidades.GREEN + "Carlos inscripto correctamente." + Utilidades.RESET);
        } catch (CupoExcedidoException e) {
            System.out.println(Utilidades.RED + e.getMessage() + Utilidades.RESET);
        }

        System.out.println(Utilidades.BLUE + "\n--- Estado final del evento ---" + Utilidades.RESET);
        evento.mostrarDatos();
        System.out.println("Cantidad de eventos: " + EventoUniversitario.getCantidadEventos());
    }

    private static void ejercicio2() {
        System.out.println(Utilidades.BLUE + "\n\n=== TP2 - EJERCICIO 2 ===" + Utilidades.RESET);

        System.out.println(Utilidades.BLUE + "\n--- Creación de estudiantes, sala, evento y actividades ---" + Utilidades.RESET);
        Estudiante ana = new Estudiante("1501", "Diego Maradona");
        Estudiante luis = new Estudiante("1502", "Manteca Martinez");

        Sala sala = new Sala(2, "Aula Magna");

        EventoUniversitario evento = new EventoUniversitario("EVT-002", "Semana Tech", 800.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Historia de la IA", 40, "Charla", "Dr. Salas", false);
        evento.crearActividad("Taller de Docker", 12, "Taller", null, true);
        evento.crearActividad("Curso de Java Avanzado", 20, "Curso", null, false, 3);

        try {
            System.out.println(Utilidades.BLUE + "\n--- Inscripción de alumnos en cada actividad ---" + Utilidades.RESET);
            for (Actividad actividad : evento.getActividades()) {
                actividad.inscribir(ana);
                actividad.inscribir(luis);
            }
            System.out.println(Utilidades.GREEN + "Diego y Martinez inscriptos en las 3 actividades." + Utilidades.RESET);

            System.out.println(Utilidades.BLUE + "\n--- Emisión de certificados (talleres y cursos, no charlas) ---" + Utilidades.RESET);
            List<String> certificados = new ArrayList<>();
            for (Actividad actividad : evento.getActividades()) {
                if (actividad instanceof Certificable certificable) {
                    for (Inscripcion inscripcion : actividad.getInscripciones()) {
                        certificados.add(certificable.generarCertificado(inscripcion.getEstudiante()));
                    }
                }
            }
            for (String certificado : certificados) {
                System.out.println(Utilidades.GREEN + certificado + Utilidades.RESET);
            }
        } catch (CupoExcedidoException e) {
            System.out.println(Utilidades.RED + e.getMessage() + Utilidades.RESET);
        }

        System.out.println(Utilidades.BLUE + "\n--- Datos del evento ---" + Utilidades.RESET);
        evento.mostrarDatos();
    }

    private static void ejercicio3() {
        System.out.println(Utilidades.BLUE + "\n\n=== TP2 - EJERCICIO 3 ===" + Utilidades.RESET);

        System.out.println(Utilidades.BLUE + "\n--- Creación de estudiantes, sala, evento y actividades ---" + Utilidades.RESET);
        Estudiante ana = new Estudiante("1601", "Cristian Pavon");
        Estudiante bruno = new Estudiante("1602", "Cristian Romero");

        Sala sala = new Sala(3, "Laboratorio 2");

        EventoUniversitario evento = new EventoUniversitario("EVT-003", "Congreso de Sistemas", 1200.0, false);
        evento.asignarSala(sala);
        evento.crearActividad("Charla de apertura", 100, "Charla", "Ing. Russo", false);
        evento.crearActividad("Taller de Kubernetes", 15, "Taller", null, true);
        evento.crearActividad("Curso de Bases de Datos", 25, "Curso", null, false, 2);

        try {
            System.out.println(Utilidades.BLUE + "\n--- Inscripción de alumnos ---" + Utilidades.RESET);
            for (Actividad actividad : evento.getActividades()) {
                actividad.inscribir(ana);
            }
            evento.getActividades().get(1).inscribir(bruno);
            System.out.println(Utilidades.GREEN + "Inscripciones realizadas." + Utilidades.RESET);

            List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
            List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
            List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

            System.out.println(Utilidades.BLUE + "\n--- Cantidad de actividades por tipo ---" + Utilidades.RESET);
            System.out.println("Charlas: " + charlas.size());
            System.out.println("Talleres: " + talleres.size());
            System.out.println("Cursos: " + cursos.size());

            System.out.println(Utilidades.BLUE + "\n--- Filtrado tipado (List<Charla>, List<Taller>, List<Curso>) ---" + Utilidades.RESET);
            System.out.println("Charlas:");
            for (Charla c : charlas) c.mostrarIdentificacion();
            System.out.println("Talleres:");
            for (Taller t : talleres) t.mostrarIdentificacion();
            System.out.println("Cursos:");
            for (Curso c : cursos) c.mostrarIdentificacion();

            System.out.println(Utilidades.BLUE + "\n--- Costo de materiales por tipo (List<? extends Actividad>) ---" + Utilidades.RESET);
            System.out.println("Costo materiales charlas: $" + evento.calcularCostoMateriales(charlas));
            System.out.println("Costo materiales talleres: $" + evento.calcularCostoMateriales(talleres));
            System.out.println("Costo materiales cursos: $" + evento.calcularCostoMateriales(cursos));
        } catch (CupoExcedidoException e) {
            System.out.println(Utilidades.RED + e.getMessage() + Utilidades.RESET);
        }
    }

    private static void ejercicio4() {
        System.out.println(Utilidades.BLUE + "\n\n=== TP2 - EJERCICIO 4 ===" + Utilidades.RESET);

        System.out.println(Utilidades.BLUE + "\n--- Creación de estudiantes, sala, evento y actividades ---" + Utilidades.RESET);
        Estudiante Blas = new Estudiante("2001", "Emiliano Martinez");
        Estudiante Martin = new Estudiante("2002", "Facundo Medina");
        Estudiante Carlos = new Estudiante("2003", "Leando Paredes");

        Sala sala = new Sala(4, "Sala de Conferencias");

        EventoUniversitario evento = new EventoUniversitario("EVT-004", "Jornada de Cierre", 0, true);
        evento.asignarSala(sala);
        evento.crearActividad("Charla de cierre", 30, "Charla", "Dra. DaSilva", false);
        evento.crearActividad("Taller de Cgit branch -M mainloud", 10, "Taller", null, true);

        try {
            Actividad charla = evento.getActividades().get(0);
            Actividad taller = evento.getActividades().get(1);

            System.out.println(Utilidades.BLUE + "\n--- Inscripciones ---" + Utilidades.RESET);
            Inscripcion insc1 = charla.inscribir(Blas);
            Inscripcion insc2 = taller.inscribir(Martin);
            Inscripcion insc3 = taller.inscribir(Carlos);
            System.out.println(Utilidades.GREEN + "3 inscripciones creadas, todas en estado Pendiente." + Utilidades.RESET);

            System.out.println(Utilidades.BLUE + "\n--- Confirmación de algunas inscripciones ---" + Utilidades.RESET);
            insc1.confirmar();
            insc2.confirmar();
            // insc3 queda a propósito sin confirmar
            System.out.println(Utilidades.GREEN + "Se confirmaron las inscripciones de Ana y Juan. La de Sofía queda pendiente." + Utilidades.RESET);

            System.out.println(Utilidades.BLUE + "\n--- Emisión de tickets (solo inscripciones confirmadas) ---" + Utilidades.RESET);
            insc1.emitirTicket();
            insc2.emitirTicket();
            Inscripcion.TicketDeAcceso ticketSofia = insc3.emitirTicket();
            if (ticketSofia == null) {
                System.out.println(Utilidades.YELLOW + "No se emite ticket para Sofía: su inscripción no está confirmada." + Utilidades.RESET);
            }

            System.out.println(Utilidades.BLUE + "\n--- Inicio del envío concurrente de tickets ---" + Utilidades.RESET);
            EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento);
            hiloEnvio.start();

            System.out.println("[" + Thread.currentThread().getName() + "] Mientras se envían los tickets en paralelo, sigo mostrando datos del evento:");
            evento.mostrarDatos();
            System.out.println("[" + Thread.currentThread().getName() + "] Estudiantes inscriptos:");
            for (Actividad actividad : evento.getActividades()) {
                actividad.mostrarInscripciones();
            }

            hiloEnvio.join();
            System.out.println(Utilidades.GREEN + "\nEnvío de tickets finalizado, ambos flujos terminaron." + Utilidades.RESET);
        } catch (CupoExcedidoException e) {
            System.out.println(Utilidades.RED + e.getMessage() + Utilidades.RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Utilidades.RED + "El hilo principal fue interrumpido esperando el envío de tickets." + Utilidades.RESET);
        }
    }
}
