package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.StringJoiner;

public class SBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    // Oppgave 1 ////////////////////////////////////////////////
    // Modifisert versjon av Programkode 5.2.3 a) //
    public boolean leggInn(T verdi) {
        // Sjekker at verdien som legges inn ikke er "Null".
        Objects.requireNonNull(verdi, "Ikke lov med Null-verdier.");

        // Starter i rotnoden.
        Node<T> gjellNode = rot; 
        // Forrige node starter som "null".
        Node<T> forNode = null; 
        // Hjelpevariabel.
        int sml = 0; 

        // Iterer til vi er "ute av treet".
        while (gjellNode != null) {
            // Nå vi lytter oss en node nedover treet er gjellende node
            // foreldre-noden til den neste noden.
            forNode = gjellNode; 
            // Bruker comparatoren til å sjekke om den nye verdien er 
            // mindre eller større / lik den gjellende noden.
            sml = comp.compare(verdi, gjellNode.verdi); // Returnerer en negativ integer hvis "verdi" < "gjellNode.verdi".

            // Hvis den nye verdien er mindre enn gjellende node,
            // flytter vi oss til det venstre barnet.
            if (sml < 0) {
                gjellNode = gjellNode.venstre;
            }
            // Hvis ikke flytter vi oss til det høyre barnet.
            else {
                gjellNode = gjellNode.høyre;
            }
        }
        
        // Oppretter en ny node med input-verdien.
        // forNode settes som foreldrenoden.
        gjellNode = new Node<T>(verdi, forNode);

        // Hvis foreldrenoden er null er treet tomt,
        // og den nye noden settes som rot-node.
        if (forNode == null) {
            rot = gjellNode;
        }
        // Hvis den nye verdien er mindre enn foreldre-noden,
        // blir den det venstre barnet.
        else if (sml < 0) {
            forNode.venstre = gjellNode;
        }
        // Hvis ikke blir den det høyre barnet.
        else {
            forNode.høyre = gjellNode;
        }

        antall++;
        return true;

    }

    // Oppgave 6 ////////////////////////////////////////////////
    public boolean fjern(T verdi) {
        // Hvis verdien er "null" blir ingenting fjernet.
        if (verdi == null) {
            return false;
        }

        // Hjelpevariabel.
        Node<T> gjellNode = null;
        // Hvis rot-noden har verdien vi søker etter,
        // sjekker vi først om det finnes en annen node med verdien.
        // Dette siden rot-noden er den siste i rekkefølgen.
        if (verdi.equals(rot.verdi)) {
            gjellNode = finnNeste(rot.høyre, verdi);
            if (gjellNode == null) {
                gjellNode = rot;
            }
        }
        else {
            gjellNode = finnNeste(rot, verdi);
        }
        
        // Hvis verdien ikke finnes i treet, blir ingenting fjernet.
        if (gjellNode == null) {
            return false;
        }

        // Fjerner noden
        fjernNode(gjellNode);
        // Senker antall.
        antall--;
        // returnerer 'true'.
        return true;
    }

    // Oppgave 6 ////////////////////////////////////////////////
    public int fjernAlle(T verdi) {
        if (tom() || verdi == null) {
            return 0;
        }
        // Hjelpebariabel.
        int antallFjernet = 0;
        // Finner første node med den gitte verdien, ved hjelp av en hjelpemetode.
        Node<T> gjellNode = finnNeste(rot, verdi);

        // Itererer så lenge det er en node med den gitte verdien.
        while (gjellNode != null) {
            // Sletter noden, finner neste node med den gitte verdien
            // i subtreet returnert av metoden.
            gjellNode = finnNeste(fjernNode(gjellNode), verdi);
            // Oppdaterer antall fjernet.
            antallFjernet++;
        }
        // Returnerer antall fjernet.
        return antallFjernet;
    }

    // Oppgave 2 ////////////////////////////////////////////////
    public int antall(T verdi) {
        // "null" finnes ikke i treet.
        if (verdi == null) {
            return 0;
        }

        // Bruker hjelpemetode til å finne første node med riktig verdi.
        // Starter søkjet i rotnoden.
        Node<T> gjellNode = finnNeste(rot, verdi);
        // Hjelpevariabel
        int antallForekomster = 0;

        // Iterer over treet.
        while (gjellNode != null) {
            antallForekomster++;
            // Vi vet at duplikatverdier vil bli plassert i et sub-tre til høyre for noden med samme verdi.
            // Søker derfor videre fra høyre barn.
            gjellNode = finnNeste(gjellNode.høyre, verdi);
        }

        return antallForekomster;
    }

    // Oppgave 6 ////////////////////////////////////////////////
    public void nullstill() {
        // Hvis treet er tomt, gjøres ingenting.
        if (tom()) {
            return;
        }
        // Oppretter en Kø.
        Queue<Node<T>> treKø = new LinkedList<>();
        // Hjelpevariabel.
        Node<T> gjellNode;

        // Legger til rot-noden i Køen.
        treKø.add(rot);
        // Itererer så lenge det er en Node i Køen.
        while (!treKø.isEmpty()) {
            // Henter ut første Noden fra Køen.
            gjellNode = treKø.remove();
            // Legger til eventuele barn i Køen.
            if (gjellNode.venstre != null) {
                treKø.add(gjellNode.venstre);
            }
            if (gjellNode.høyre != null) {
                treKø.add(gjellNode.høyre);
            }
            // Nuller ut alle pekere.
            gjellNode.verdi = null;
            gjellNode.forelder = null;
            gjellNode.venstre = null;
            gjellNode.høyre = null;
        }
        // Setter "rot" til 'null'.
        rot = null;
        // Setter "antall" til '0'.
        antall = 0;
    }

    // Oppgave 3 ////////////////////////////////////////////////
    private static <T> Node<T> førstePostorden(Node<T> p) {
        // Hjelpevariabel.
        Node<T> gjellNode = p;
        // Bruker en While-løkke til å iterer gjennom treet.
        while (true) {
            // Hvis noden har et venstre barn går vi dit.
            if (gjellNode.venstre != null) {
                gjellNode = gjellNode.venstre;
            }
            // Hvis ikke går vi til høyre barn, gitt at dette eksisterer.
            else if (gjellNode.høyre != null) {
                gjellNode = gjellNode.høyre;
            }
            // Hvis noden ikke har noen barn er vi ved den riktige noden,
            // og denne returneres.
            else {
                return gjellNode;
            }
        }
    }

    // Oppgave 3 ////////////////////////////////////////////////
    private static <T> Node<T> nestePostorden(Node<T> p) {
        // Den siste i postorden vil alltid være rotnoden.
        // Hvis "p" er rotnoden vil den ikke ha en forwwelder, i såfall returneres 'null'.
        if (p.forelder == null) {
            return null;
        }

        Node<T> gjellNode = p;

        // Hvis foreldrenoden ikke har et høyre barn, returneres foreldre-noden.
        if (gjellNode.forelder.høyre == null) {
            return gjellNode.forelder;
        }
        // Hvis input-noden er høyrebarnet til foreldre-noden,
        // returneres foreldre-noden.
        else if (gjellNode.forelder.høyre.equals(gjellNode)) {
            return gjellNode.forelder;
        }
        // Hvis ikke, vet vi at input-noden er har et høyre søsken.
        // Kaller da første Postorder med søskenet som rot-node og returnerer den returnerte noden.
        else {
            return førstePostorden(gjellNode.forelder.høyre);
        }
    }

    // Oppgave 4 ////////////////////////////////////////////////
    public void postorden(Oppgave<? super T> oppgave) {
        // Hvis treet er tomt gjøres ingenting.
        if (tom()) {
            return;
        }

        // Finner første node i postorden.
        Node<T> gjellNode = førstePostorden(rot);

        // Iterer gjennom treet.
        while (gjellNode != null) {
            // Utfører oppgaven.
            oppgave.utførOppgave(gjellNode.verdi);
            // Finner neste node i postorden.
            gjellNode = nestePostorden(gjellNode);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    // Oppgave 4 ////////////////////////////////////////////////
    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        // Hvis det finnes et venstre barn går vi dit.
        if (p.venstre != null) {
            postordenRecursive(p.venstre, oppgave);
        }
        // Hvis det finnest et høyre barn går vi dit.
        if (p.høyre != null) {
            postordenRecursive(p.høyre, oppgave);
        }
        // Når vi har nådd en løv-node, utfører vi oppgaven.
        oppgave.utførOppgave(p.verdi);
    }

    // Oppgave 5 ////////////////////////////////////////////////
    public ArrayList<T> serialize() {
        // Oppretter en Kø og en ArrayListe.
        Queue<Node<T>> treKø = new LinkedList<Node<T>>();
        ArrayList<T> treListe = new ArrayList<>();

        // legger til roten i listen.
        treKø.add(rot);

        // Iterer så lenge det er et element i Køen.
        while (!treKø.isEmpty()) {
            // Henter første element i listen.
            Node<T> gjellNode = treKø.remove();
            // Legger til verdien i ArrayListen.
            treListe.add(gjellNode.verdi);
            // Legger til eventuelle barn i Køen.
            if (gjellNode.venstre != null) {
                treKø.add(gjellNode.venstre);
            }
            if (gjellNode.høyre != null) {
                treKø.add(gjellNode.høyre);
            }
        }
        // Når det ikke lenger er elementer i Køen,
        // returneres ArrayListen.
        return treListe;
    }

    // Oppgave 5 ////////////////////////////////////////////////
    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        // Oppretter et tomt tre.
        SBinTre<K> nyttTre = new SBinTre<>(c);

        // Iterer over arraylisten.
        for (K var : data) {
            // legger til hver verdi som en ny node.
            nyttTre.leggInn(var);
        }

        return nyttTre;
    }

    // Hjelpemetoder /////
    
    // Metode for å finne neste node med en gitt verdi, med start på en gitt node.
    private Node<T> finnNeste(Node<T> startNode, T verdi) {
        // Starter i startnoden.
        Node<T> gjellNode = startNode;
        // Hjelpevariabel.
        int sml = 0;

        // Iterer gjennom treet.
        while (gjellNode != null) {
            // Sjekker om verdien er mindre, lik eller større enn gjellende node.
            sml = comp.compare(verdi, gjellNode.verdi);
            // Bryter løkken hvis det er likt.
            if (sml == 0) {
                break;
            }
            // Går til venstre barn hvis den er mindre.
            else if (sml < 0) {
                gjellNode = gjellNode.venstre;
            }
            // Går til høyre barn hvis den er større.
            else {
                gjellNode = gjellNode.høyre;
            }
        }

        return gjellNode;
    }

    // Metode for å fjerne en gitt node.
    // Returnerer noden som tar dens plass.
    private Node<T> fjernNode(Node<T> fjernetNode) {
        // Hjelpevariabler
        Node<T> subTre;
        Node<T> venstrePlass;

        // Hvis begge barna er null, blir det ikke et subtre.
        if (fjernetNode.venstre == null && fjernetNode.høyre == null) {
            subTre = null;
        }
        // Hvis noden kun har et barn, blir subtreet det barnet.
        else if (fjernetNode.venstre == null) {
            subTre = fjernetNode.høyre;
        }
        else if (fjernetNode.høyre == null) {
            subTre = fjernetNode.venstre;
        }
        // Hvis noden har to barn, blir subtreet det høyre barnet,
        // med det venstre barnet plassert som venstre barn til noden i første postorden.
        else {
            subTre = fjernetNode.høyre;
            venstrePlass = førstePostorden(subTre);
            venstrePlass.venstre = fjernetNode.venstre;
            fjernetNode.venstre.forelder = venstrePlass;
        }

        // Hvis noden som fjernes er roten,  settes rot som subtreet, og subtreets forelder settes som null.
        if (fjernetNode == rot) {
            rot = subTre;
            if (subTre != null) {
                subTre.forelder = null;
            }
        }
        // Hvis det ikke er et subtre, sjekkes det hvilket barn noden er,
        // og den aktuelle pekeren i forelder-noden settes til null.
        else if (subTre == null) {
            if (fjernetNode.forelder.venstre == fjernetNode) {
                fjernetNode.forelder.venstre = null;
            }
            else {
                fjernetNode.forelder.høyre = null;
            }
        }
        // Hvis det er et subtre sjekkes det samme,
        // men pekeren i forelder-noden settes til å peke på subtreet,
        // og subtreet peker til foreder-noden.
        else {
            if (fjernetNode.forelder.venstre == fjernetNode) {
                fjernetNode.forelder.venstre = subTre;
                subTre.forelder = fjernetNode.forelder;
            }
            else {
                fjernetNode.forelder.høyre = subTre;
                subTre.forelder = fjernetNode.forelder;
            }
        }

        // Noden som fjernes nulles ut.
        fjernetNode.verdi = null;
        fjernetNode.høyre = null;
        fjernetNode.venstre = null;
        fjernetNode.forelder = null;
        // Subtreet returneres.
        return subTre;
    }
    
} // ObligSBinTre
