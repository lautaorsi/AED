package aed;

public class SistemaSIU { // Invariante: 
    // Cada alumno inscripto en una materia debe estar en trie alumnos
    // Cada alumno puede estar inscripto únicamente a materias de su/s carrera/s
    // Los nombres de las materias dentro de una carrera deben ser únicos
    // Todos los strings LU tienen una misma longitud maxima
    //El valor asociado a cada alumno en trie alumnos refleja la cantidad de materias a las que esta inscripto
    //Cada alumno tiene un LU unico
    //Si una materia tiene un sinonimo en otra/s carrera/s, se debe poder acceder desde esa/s otra/s carrera/s
    //y deben estar sincronizados todos sus atributos (docentes, alumnos,inscriptos)
    //Cada alumno aparece en tantas materias como indique su cantidad de materias inscriptas, 
    //y estas deben ser diferentes entre si
    //La cantidad de inscriptos en una materia no supera a la cantidad de claves del trie alumnos
    //Cada carrera esta registrada en trie carreras
    //Todas las materias están asociadas al menos a 1 carrera
    //Cada carrera debe tener al menos 1 materia asociada
    // Un alumno no puede estar inscripto en la misma materia mas de una vez
    //La cantidad de materias de un alumno no supera la cantidad de materias del sistema

    private DictTrie<Carrera> carreras;
    private DictTrie<Integer> alumnos;

    enum CargoDocente {
        AY2,
        AY1,
        JTP,
        PROF
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.carreras = new DictTrie<Carrera>(); // O(1)
        this.alumnos = new DictTrie<Integer>(); // O(1)
        for (InfoMateria materia : infoMaterias) { // se ejecuta k veces (k=longitud infoMaterias = cantMaterias)
            Materia nuevaMateria = new Materia(); // O(1)
            for (ParCarreraMateria par : materia.getParesCarreraMateria()) {  // ejecuta h veces 
                                                                     //(h=cant de carreras/nombres de materia)                                                      
                String nombreCarrera = par.getCarrera(); // O(1)
                Carrera carreraDestino; // O(1)
                if (!(this.carreras.pertenece(nombreCarrera))) { // O(|c|)
                    carreraDestino = new Carrera(); // O(1)
                    carreras.agregar(nombreCarrera, carreraDestino); // O(|c|)
                } else {
                    carreraDestino = carreras.devolver(nombreCarrera); // O(|c|)
                }
                carreraDestino.agregar(par.getNombreMateria(), nuevaMateria); // O(|m|) ->para cada nombre de la materia
                nuevaMateria.agregarSinonimo(carreraDestino, par.getNombreMateria()); // O(1)
            }
        }
        for (String alumno : libretasUniversitarias) { // ejecuta t veces (t=cantidad estudiantes=E)
            this.alumnos.agregar(alumno, 0); // O(1) -> strings LU acotados
        }
    } // O(E + sum(m E M)sum(n E Nm)(|n|) + sum(c E C)(|c|*|Mc|))

    public void inscribir(String estudiante, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|) ->nombre de carrera
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|) -> nombre de materia
        if (!materiaDestino.devolverConjuntoAlumnos().pertenece(estudiante)) {
            materiaDestino.inscribirAlumno(estudiante); // O(1)
            int nMateriasAlumno = this.alumnos.devolver(estudiante); // O(1) -> porque LU son acotadas
            nMateriasAlumno += 1; // O(1)
            this.alumnos.agregar(estudiante, nMateriasAlumno); // O(1)
        }

    }  //A cada carrera le añadimos materias tantas veces como pares carrera-nombreMateria tengan a dicha carrera (|Mc|),
    //de ahí obtenemos(sum(c E C)(|c|*|Mc|)). 
    //A su vez, cada materia la agregamos tantas veces como pares carrera-nombreMateria tenga su lista infoMateria,
    //obteniendo (sum(m E M)sum(n E Nm)(|n|)), siendo n los nombres de la materia.
    //Se tiene en cuenta que tanto los nombres de materias como los de carreras NO son acotados.
    //Cada LU de libretasUniversitarias lo agregamos al trie de alumnos. Al ser acotadas las LU, la complejidad
    //de las operaciones es O(1), por lo que el costo depende únicamente de la cantidad de LU agregados (E).
    //Sumando las complejidades: O(E + sum(m E M)sum(n E Nm)(|n|) + sum(c E C)(|c|*|Mc|))

    
    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|)
        materiaDestino.agregarDocente(cargo); // O(1)
    } // O(|c|+|m|)

    public int[] plantelDocente(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|)
        return materiaDestino.plantelDocente(); // O(1)
    } // O(|c|+|m|)

    public void cerrarMateria(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|)
        materiaDestino.cerrarMateria(this.alumnos); // O(Em + sum(n E Nm)(|n|))
    } // O(|c|+|m|+Em+sum(n E Nm)(|n|))

    public int inscriptos(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|)
        return materiaDestino.inscriptos(); // O(1)
    } // O(|c|+|m|)

    public boolean excedeCupo(String materia, String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        Materia materiaDestino = carreraDestino.devolver(materia); // O(|m|)
        return materiaDestino.excedido(); // O(1)
    } // O(|c|+|m|)

    public String[] carreras() {
        return carreras.listar();
    } //el método listar() tiene complejidad O(n+k). n corresponde a la cantidad de nodos, lo cual podemos aproximar
    //a la suma de la longitud de todas las claves. En el peor caso, que no comparten ningún prefijo las carreras,
    //la cantidad de nodos y la suma de la longitud de las claves serán iguales. Suponemos además que k(cantidad
    //de carreras) es pequeño frente a dicha suma. Así que acotamos por  O(sum(c E C)(|c|))

    public String[] materias(String carrera) {
        Carrera carreraDestino = this.carreras.devolver(carrera); // O(|c|)
        return carreraDestino.devolverMaterias().listar();
    } //Obtenemos las materias de la carrera (O|c|). Idem al caso anterior, listar tiene complejidad O(n+k),
    //siendo n la cantidad de nodos, pudiendo esta ser aproximada a la suma de la longitud de los nombres de las materias
    //de la carrera especificada. Suponemos que k es pequeño frente a la suma. 
    //Quedando la complejidad O(|c|+sum(mc E Mc)(|mc|))

    public int materiasInscriptas(String estudiante) {
        return alumnos.devolver(estudiante); // O(1) -> debido a LU ACOTADA
    }
}
