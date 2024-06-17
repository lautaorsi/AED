package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SistemaSIU {
    private DictTrie<Carrera> carreras;
    private DictTrie<Integer> alumnos;

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

    private class DictTrie<V> {
        private Nodo raiz;

        private class Nodo{
            ArrayList<Nodo> sig; //O(1)
            String clave;
            V val=null; //O(1)
            int hijosNoNulo; //O(1)

            public Nodo() {
                sig = new ArrayList<Nodo>();
                // al ejecutarse siempre 255 veces tiene complejidad O(255) = O(1)
                for (int i = 0; i < 256; i++) {
                    sig.add(null);
                } 
                hijosNoNulo=0; //O(1)
            }
        
        }
        public DictTrie(){
            raiz = new Nodo(); //O(1)
        }

        public Boolean pertenece(String clave) {
            Nodo actual=raiz; //O(1)
            // este for tiene complejidad O(|s|) ya que en el peor caso cicla por todos los caracteres del String
            for (int i = 0; i < clave.length(); i++){
                int c = (int) clave.charAt(i); //O(1)
                if(actual.sig.get(c)==null){ //O(1)
                    return false; //O(1)
                } else {
                    actual=actual.sig.get(c); //O(1)
                }
            }
            return actual.val != null; //O(1)
        }

        public void agregar(String clave, V valor) {
            Nodo actual=raiz; //O(1)
            // este for tiene complegidad O(|s|) ya que siempre cicla por todos los caracteres del String
            for (int i=0; i<clave.length(); i++){ 
                int c = (int) clave.charAt(i); //supongo que es O(1)
                if(actual.sig.get(c)==null){  //O(1)
                    Nodo n = new Nodo();
                    actual.sig.set(c, n);  //O(1)
                    actual.hijosNoNulo++;  //O(1)
                }
                actual=actual.sig.get(c);  //O(1)
            }
            actual.val=valor;  //O(1)
            actual.clave=clave;
        }

        public void quitar(String clave){
            Nodo actual = raiz; //O(1)
            Nodo ultimoUtil=raiz; //O(1)
            int ultimoCUtil=0; //O(1)
            // este for tiene complegidad O(|s|) ya que siempre cicla por todos los caracteres del String
            for (int i=0; i<clave.length(); i++){
                int c = (int) clave.charAt(i); //O(1)
                if(actual.hijosNoNulo>1){ //O(1)
                    ultimoCUtil = c; //O(1)
                    ultimoUtil = actual; //O(1)
                }
                actual=actual.sig.get(c); //O(1)
            }
            if(actual.hijosNoNulo==0){ //O(1)
                ultimoUtil.sig.set(ultimoCUtil, null); //O(1)
            } else {
                actual.val=null; //O(1)
                actual.clave=null;
            }
        }

        public V devolver(String clave) {
            Nodo actual=raiz;
            for (int i=0; i<clave.length(); i++){
                int c = (int) clave.charAt(i);
                if(actual.sig.get(c)==null){
                    return null;
                } else {
                    actual=actual.sig.get(c);
                }
            }
            return actual.val;
        }

        public String[] listar(){
            ArrayList<String> list = new ArrayList<String>();

            list=listaRecursiva(list, raiz);

            String[] res = new String[list.size()];
            res = list.toArray(res);
            return res;
        }

        private ArrayList<String> listaRecursiva(ArrayList<String> lista, Nodo actual){
            if(actual.clave!=null){
                lista.add(actual.clave);
            }
            if(actual.hijosNoNulo>0){
                for (int i = 0; i < 256; i++) {
                    if(actual.sig.get(i)!=null){
                        lista = listaRecursiva(lista, actual.sig.get(i));
                    }
                }
            }
            return lista;
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
            Tupla<Carrera, String> nuevoSinonimo = new Tupla<Carrera, String>(carrera, nombreMateria);
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
        private DictTrie<Materia> materias;

        public Carrera(){
            this.materias = new DictTrie<Materia>();
        }

        public void agregar(String nombreMateria, Materia materia) {
            materias.agregar(nombreMateria, materia);
        }

        public Materia devolver(String nombreMateria) {
            return materias.devolver(nombreMateria);
        }
        

    }

    enum CargoDocente {
        AY2,
        AY1,
        JTP,
        PROF
    }

    // LISTO
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.carreras = new DictTrie<Carrera>();
        this.alumnos = new DictTrie<Integer>();
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
        throw new UnsupportedOperationException("Método no implementado aún");/////////////////////////////////NO IMPLEMENTADO AUN
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
        return carreras.listar();
    }

    public String[] materias(String carrera) {
        return carreras.devolver(carrera).materias.listar();
    }

    public int materiasInscriptas(String estudiante) {
        return alumnos.devolver(estudiante);
    }
}
