package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SistemaSIU {
    private DictTrie<String, Carrera> carreras;
    private DictTrie<String, Integer> alumnos;

    private class Tupla<A, B> {
        private A primero;
        private B segundo;

        public Tupla(A primero, B segundo) {
            this.primero = primero;
            this.segundo = segundo;
        }

        public A devolverPrimero() {
            return primero;
        }

        public B devolverSegundo() {
            return segundo;
        }
    }

    private class DictTrie<K, V> {
        // TODO: implementar un diccionario con un trie, con clave tipo K y valor tipo
        // V.
        public Boolean pertenece(K clave) {
            // TODO: implementar
            throw new UnsupportedOperationException("Método no implementado aún");
        }

        public void agregar(K clave, V valor) {
            // TODO: implementar
        }

        public V devolver(K clave) {
            // TODO: implementar
            throw new UnsupportedOperationException("Método no implementado aún");
        }
    }

    // CREO QUE ESTA CLASE ESTÁ LISTA
    private class Materia {
        private int[] plantelDocente;
        private ListaEnlazada<Tupla<Carrera, String>> listaSinonimos;
        private ListaEnlazada<String> listaAlumnos;
        private int inscriptosMateria;

        public Materia() {
            this.plantelDocente = new int[] { 0, 0, 0, 0 };
            this.listaSinonimos = new ListaEnlazada<Tupla<Carrera, String>>();
            this.listaAlumnos = new ListaEnlazada<String>();
            this.inscriptosMateria = 0;
        }

        public void agregarSinonimo(Carrera carrera, String nombreMateria) {
            Tupla<Carrera, String> nuevoSinonimo = new Tupla(carrera, nombreMateria);
            this.listaSinonimos.agregarAtras(nuevoSinonimo);
        }

        public void inscribirAlumno(String alumno) {
            this.inscriptosMateria += 1;
            this.listaAlumnos.agregarAtras(alumno);
        }

        public void agregarDocente(CargoDocente cargo) {
            switch (cargo) {
                case PROF:
                    this.plantelDocente[0] += 1;
                    break;
                case JTP:
                    this.plantelDocente[1] += 1;
                    break;
                case AY1:
                    this.plantelDocente[2] += 1;
                    break;
                case AY2:
                    this.plantelDocente[3] += 1;
            }
        }

        public int[] plantelDocente() {
            return plantelDocente;
        }

        public int inscriptos() {
            return inscriptosMateria;
        }

        public boolean excedido() {
            Boolean res;
            Boolean condProf = 250 * plantelDocente[0] < inscriptosMateria;
            Boolean condJTP = 100 * plantelDocente[1] < inscriptosMateria;
            Boolean condAY1 = 20 * plantelDocente[2] < inscriptosMateria;
            Boolean condAY2 = 20 * plantelDocente[3] < inscriptosMateria;
            res = (condProf || condJTP || condAY1 || condAY2);
            return res;
        }
    }

    private class Carrera {
        private DictTrie<String, Materia> materias;

        public void agregar(String nombreMateria, Materia materia) {
            materias.agregar(nombreMateria, materia);
        }

        public Materia devolver(String nombreMateria) {
            throw new UnsupportedOperationException("Método no implementado aún");
        }
        // TODO: definir e implementar los métodos de la clase.

    }

    enum CargoDocente {
        AY2,
        AY1,
        JTP,
        PROF
    }

    // LISTO
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.carreras = new DictTrie<String, Carrera>();
        this.alumnos = new DictTrie<String, Integer>();
        for (InfoMateria materia : infoMaterias) {
            Materia nuevaMateria = new Materia();
            for (ParCarreraMateria par : materia.getParesCarreraMateria()) {
                String nombreCarrera = par.getCarrera();
                Carrera carreraDestino;
                if (!(this.carreras.pertenece(nombreCarrera))) {
                    carreraDestino = new Carrera();
                    carreras.agregar(nombreCarrera, carreraDestino);
                } else {
                    carreraDestino = carreras.devolver(nombreCarrera);
                }
                carreraDestino.agregar(par.getNombreMateria(), nuevaMateria);
                nuevaMateria.agregarSinonimo(carreraDestino, par.getNombreMateria());
            }
        }
        for (String alumno : libretasUniversitarias) {
            this.alumnos.agregar(alumno, 0);
        }
    }

    // LISTO
    public void inscribir(String estudiante, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera);
        Materia materiaDestino = carreraDestino.devolver(materia);
        materiaDestino.inscribirAlumno(estudiante);
        int nMateriasAlumno = this.alumnos.devolver(estudiante);
        nMateriasAlumno += 1;
        this.alumnos.agregar(estudiante, nMateriasAlumno);
    }

    // LISTO
    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera);
        Materia materiaDestino = carreraDestino.devolver(materia);
        materiaDestino.agregarDocente(cargo);
    }

    // LISTO
    public int[] plantelDocente(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera);
        Materia materiaDestino = carreraDestino.devolver(materia);
        return materiaDestino.plantelDocente();
    }

    public void cerrarMateria(String materia, String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public int inscriptos(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera);
        Materia materiaDestino = carreraDestino.devolver(materia);
        return materiaDestino.inscriptos();
    }

    public boolean excedeCupo(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera);
        Materia materiaDestino = carreraDestino.devolver(materia);
        return materiaDestino.excedido();
    }

    public String[] carreras() {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public String[] materias(String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public int materiasInscriptas(String estudiante) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }
}
