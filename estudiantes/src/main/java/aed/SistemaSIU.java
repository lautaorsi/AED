package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SistemaSIU { //Invariante: no se puede repetir un mismo LU en una materia. 
    //Cada alumno inscripto en una materia debe estar en trie alumnos
    //Cada alumno puede estar inscripto únicamente a materias de su/s carrera/s
    //Los nombres de las materias dentro de una carrera deben ser únicos
    //Todos los strings LU tienen una misma longitud maxima
    //cada alumno tiene un LU unico
    //Cada alumno aparece en tantas materias como indique su cantidad de materias inscriptas, y estas deben
    //ser diferentes entre si
    //la cantidad de inscriptos en una materia debe ser menor o igual a la cantidad de claves del trie alumnos
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

    private class DictTrie<V> { //Invariante: no se llega por 2 claves al mismo nodo
        //si los nodos no tienen significado (val), tienen hijos. 
        //No hay nodos huerfanos (no alcanzables por la raiz)
        //Los valores no son nulos si tienen una clave asociada
        //Las claves no son vacías
        private Nodo raiz;

        private class Nodo {
            ArrayList<Nodo> sig; // O(1)
            String clave;
            V val; // O(1)
            int hijosNoNulo; // O(1)

            public Nodo() {
                sig = new ArrayList<Nodo>();
                // al ejecutarse siempre 255 veces tiene complejidad O(255) = O(1)
                for (int i = 0; i < 256; i++) {
                    sig.add(null);
                }
                hijosNoNulo = 0; // O(1)
                val = null;
            }

        }

        public DictTrie() {
            raiz = new Nodo(); // O(1)
        }

        public Boolean pertenece(String clave) {
            Nodo actual = raiz; // O(1)
            // este for tiene complejidad O(|s|) ya que en el peor caso cicla por todos los
            // caracteres del String
            for (int i = 0; i < clave.length(); i++) {
                int c = (int) clave.charAt(i); // O(1)
                if (actual.sig.get(c) == null) { // O(1)
                    return false; // O(1)
                } else {
                    actual = actual.sig.get(c); // O(1)
                }
            }
            return actual.val != null; // O(1)
        }

        public void agregar(String clave, V valor) {
            Nodo actual = raiz; // O(1)
            // este for tiene complegidad O(|s|) ya que siempre cicla por todos los
            // caracteres del String
            for (int i = 0; i < clave.length(); i++) {
                int c = (int) clave.charAt(i); // supongo que es O(1)
                if (actual.sig.get(c) == null) { // O(1)
                    Nodo n = new Nodo();
                    actual.sig.set(c, n); // O(1)
                    actual.hijosNoNulo++; // O(1)
                }
                actual = actual.sig.get(c); // O(1)
            }
            actual.val = valor; // O(1)
            actual.clave = clave;
        } 

        public void quitar(String clave) {
            Nodo actual = raiz; // O(1)
            Nodo ultimoUtil = raiz; // O(1)
            int ultimoCUtil = clave.charAt(0); // O(1)
            // este for tiene complegidad O(|s|) ya que siempre cicla por todos los
            // caracteres del String
            for (int i = 0; i < clave.length(); i++) {
                int c = (int) clave.charAt(i); // O(1)
                if (actual.hijosNoNulo > 1) { // O(1)
                    ultimoCUtil = c; // O(1)
                    ultimoUtil = actual; // O(1)
                }
                actual = actual.sig.get(c); // O(1)
            }
            if (actual.hijosNoNulo == 0) { // O(1)
                ultimoUtil.sig.set(ultimoCUtil, null); // O(1)
            } else {
                actual.val = null; // O(1)
                actual.clave = null;
            }
        }

        public V devolver(String clave) {
            Nodo actual = raiz; //O(1)
            for (int i = 0; i < clave.length(); i++) { //O(|s|) -> cicla por caracteres de string
                int c = (int) clave.charAt(i);
                if (actual.sig.get(c) == null) {
                    return null;
                } else {
                    actual = actual.sig.get(c);
                }
            }
            return actual.val;
        }

        public String[] listar() { //Se recorren todas las claves (O(n*max(k)))-> n = cantidad de claves,
            //max(k) = maximo tamaño de clave
            ArrayList<String> list = new ArrayList<String>(); 

            list = listaRecursiva(list, raiz);

            String[] res = new String[list.size()]; 
            res = list.toArray(res);  
            return res;
        }

        private ArrayList<String> listaRecursiva(ArrayList<String> lista, Nodo actual) {
            if (actual.clave != null) { 
                lista.add(actual.clave);
            }
            if (actual.hijosNoNulo > 0) { 
                for (int i = 0; i < 256; i++) {
                    if (actual.sig.get(i) != null) { 
                        lista = listaRecursiva(lista, actual.sig.get(i));
                    }
                }
            }
            return lista;
        }
    }

    
    private class Materia { //Invariante: el largo de listaAlumnos es igual a inscriptosMateria. 
        //listaSinonimos no tiene tuplas duplicadas
        //El largo de plantelDocente es 4
        //listaAlumnos no tiene alumnos repetidos
        //Los int en plantelDocente y inscriptosMateria son mayores o iguales a 0
        
        private int[] plantelDocente;
        private ListaEnlazada<Tupla<Carrera, String>> listaSinonimos;
        private ListaEnlazada<String> listaAlumnos;
        private int inscriptosMateria;

        public Materia() {
            this.plantelDocente = new int[] { 0, 0, 0, 0 }; //O(1)
            this.listaSinonimos = new ListaEnlazada<Tupla<Carrera, String>>(); //O(1)
            this.listaAlumnos = new ListaEnlazada<String>(); //O(1)
            this.inscriptosMateria = 0; //O(1)
        }

        public void agregarSinonimo(Carrera carrera, String nombreMateria) {
            Tupla<Carrera, String> nuevoSinonimo = new Tupla<Carrera, String>(carrera, nombreMateria); //O(1)
            this.listaSinonimos.agregarAtras(nuevoSinonimo); //O(1)
        }

        public void inscribirAlumno(String alumno) {
            this.inscriptosMateria += 1; //O(1)
            this.listaAlumnos.agregarAtras(alumno); //O(1)
        }

        public void agregarDocente(CargoDocente cargo) { //O(1)
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

        public int[] plantelDocente() { //O(1)
            return plantelDocente;
        }

        public int inscriptos() { //O(1)
            return inscriptosMateria;
        }

        public boolean excedido() { //O(1)
            Boolean res;
            Boolean condProf = 250 * plantelDocente[0] < inscriptosMateria;
            Boolean condJTP = 100 * plantelDocente[1] < inscriptosMateria;
            Boolean condAY1 = 20 * plantelDocente[2] < inscriptosMateria;
            Boolean condAY2 = 20 * plantelDocente[3] < inscriptosMateria;
            res = (condProf || condJTP || condAY1 || condAY2);
            return res;
        }

        public void cerrarMateria() {
            Iterador<String> iterAlumnos = this.listaAlumnos.iterador(); //O(1)
            while (iterAlumnos.haySiguiente()) { //se ejecuta k veces 
                String alumno = iterAlumnos.siguiente(); //O(1)
                int nMateriasAlumno = alumnos.devolver(alumno); //O(1)
                nMateriasAlumno -= 1; //O(1)
                alumnos.agregar(alumno, nMateriasAlumno); //O(1)
            } //O(1) + sum(k)(O(1)) = O(1) + k*O(1) = O(k). k = longitud de listaAlumnos = cantidad de alumnos
            // = Em. Entonces O(k) = O(Em)

            Iterador<Tupla<Carrera, String>> iterSinonimos = this.listaSinonimos.iterador(); //O(1)
            while (iterSinonimos.haySiguiente()) { //se ejectuta h veces 
                Tupla<Carrera, String> par = iterSinonimos.siguiente(); //O(1)
                Carrera carreraDestino = par.devolverPrimero(); //O(1)
                String nombreMateria = par.devolverSegundo(); //O(1)
                carreraDestino.remover(nombreMateria); //O(|m|)
            } //este while recorre listaSinonimos cuya longitud h indica la cantidad de nombres de la materia
        } //y en cada iteracion se elimina c/u de esos nombres de sus respectivas carreras (O(|m|)). Así que se puede
        //simplificar la complejidad como la sumatoria de la longitud de c/u de los nombres de la materia
        //sum(n E Nm)(|n|)
    } //cerrarMateria = O(Em + sum(n E Nm)(|n|))

    private class Carrera {
        private DictTrie<Materia> materias;

        public Carrera() {
            this.materias = new DictTrie<Materia>(); //O(1)
        }

        public void agregar(String nombreMateria, Materia materia) { 
            materias.agregar(nombreMateria, materia); //O(|m|)
        }

        public Materia devolver(String nombreMateria) {
            return materias.devolver(nombreMateria); //O(|m|)
        }

        public void remover(String nombreMateria) {
            this.materias.quitar(nombreMateria); //O(|m|)
        }

    }

    enum CargoDocente {
        AY2,
        AY1,
        JTP,
        PROF
    }

    
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.carreras = new DictTrie<Carrera>(); //O(1)
        this.alumnos = new DictTrie<Integer>(); //O(1)
        for (InfoMateria materia : infoMaterias) { //se ejecuta k veces (k=longitud infoMaterias = cantMaterias = |M|)
            Materia nuevaMateria = new Materia(); //O(1)
            for (ParCarreraMateria par : materia.getParesCarreraMateria()) { //ejecuta h veces (h=cant nombres de materia =|Nm|por materia)
                String nombreCarrera = par.getCarrera(); //O(1)
                Carrera carreraDestino; //O(1)
                if (!(this.carreras.pertenece(nombreCarrera))) { //O(|c|)
                    carreraDestino = new Carrera(); //O(1)
                    carreras.agregar(nombreCarrera, carreraDestino); //O(|c|)
                } else {
                    carreraDestino = carreras.devolver(nombreCarrera); //O(|c|)
                }
                carreraDestino.agregar(par.getNombreMateria(), nuevaMateria); //O(|m|) ->para cada materia
                nuevaMateria.agregarSinonimo(carreraDestino, par.getNombreMateria()); //O(1)
            }
        }
        for (String alumno : libretasUniversitarias) { //ejecuta t veces (t=cantidad estudiantes=E)
            this.alumnos.agregar(alumno, 0); //O(1) -> strings LU acotados
        }
    } //  O(E + sum(m E M)sum(n E Nm)(|n|) + sum(c E C)(|c|*|Mc|))

    
    public void inscribir(String estudiante, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|) ->nombre de carrera
        Materia materiaDestino = carreraDestino.devolver(materia); //O(|m|) -> nombre de materia
        materiaDestino.inscribirAlumno(estudiante); //O(1)
        int nMateriasAlumno = this.alumnos.devolver(estudiante); //O(1) -> porque LU son acotadas
        nMateriasAlumno += 1; //O(1)
        this.alumnos.agregar(estudiante, nMateriasAlumno); //O(1)
    } //O(1) + O(|c|) + O(|m|) = O(|c| + |m|)

    // LISTO
    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia);  //O(|m|)
        materiaDestino.agregarDocente(cargo); //O(1)
    } //O(|c|+|m|)

    
    public int[] plantelDocente(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); //O(|m|)
        return materiaDestino.plantelDocente(); //O(1)
    } //O(|c|+|m|)

    public void cerrarMateria(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); //O(|m|)
        materiaDestino.cerrarMateria(); //O(Em + sum(n E Nm)(|n|))
    } //O(|c|+|m|+Em+sum(n E Nm)(|n|))

    public int inscriptos(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); //O(|m|)
        return materiaDestino.inscriptos(); // O(1)
    } //O(|c|+|m|)

    public boolean excedeCupo(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); //O(|m|)
        return materiaDestino.excedido(); //O(1)
    } //O(|c|+|m|)

    public String[] carreras() {
        return carreras.listar(); 
    }//O(sum(c E C)(|c|)) 

    public String[] materias(String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); //O(|c|)
        return carreraDestino.materias.listar(); 
    }//O(|c| + sum(mc E Mc)(|mc|)) 

    public int materiasInscriptas(String estudiante) {
        return alumnos.devolver(estudiante); //O(1) -> debido a LU ACOTADA
    }
}



