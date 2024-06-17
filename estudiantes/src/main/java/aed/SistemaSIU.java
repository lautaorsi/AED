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

    private class Materia {
        private int[] plantelDocente;
        private ArrayList<Tupla<Carrera, String>> listaSinonimos;
        private ArrayList<String> listaAlumnos;
        private int inscriptos;

        public void agregarSinonimo(Carrera carrera, String nombreMateria) {
            Tupla<Carrera, String> nuevoSinonimo = new Tupla(carrera, nombreMateria);
            this.listaSinonimos.add(nuevoSinonimo);
        }

        public void inscribirAlumno(String alumno) {
            this.inscriptos += 1;
            this.listaAlumnos.add(alumno);
        }
        // TODO: definir e implementar los métodos de la clase.

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

    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public int[] plantelDocente(String materia, String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public void cerrarMateria(String materia, String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public int inscriptos(String materia, String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public boolean excedeCupo(String materia, String carrera) {
        throw new UnsupportedOperationException("Método no implementado aún");
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
