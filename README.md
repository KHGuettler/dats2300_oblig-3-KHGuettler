# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:

* Knut høie Guettler, s350775, s350775@oslomet.no

## Oppgavebeskrivelse

### Oppgave 1

Oppgaven er løst ved å bruke fremgangsmåten vist i **Programkode 5.2.3 a)**.

Treet itereres gjennom fra rot-noden. For hver node sjekkes det om den nye verdien er mindre eller lik / større enn den gjellende nodens verdi.

Hvis verdien er mindre flytter man seg til det høyre barnet, hvis ikke til det venstre barnet. Når man er utenfor treet (altså at den gjellende noden er "null") opprettes en ny node som peker på den forrige noden. Den forrige noden blir også oppdatert til å peke på den nye noden som enten høyre eller venstre barn respektivt.

### Oppgave 2

Oppgaven er løst ved å skrive en hjelpemetode som finner neste node med en gitt verdi, der søket starter på en gitt node. I selve løsnings-metoden brukes denne hjelpemetoden først til å finne det første tilfellet (søker fra rot-noden). Så kalles hjelpemetoden med samme verdi, men med start i den forrige nodens høyre barn. En variabel 'antall' teller antall forekomster, og returerneres når det ikke lenger er flere forekomster av verdien.

### Oppgave 3

Metoden førstePostorden bruker en while-løkke til å iterere nedover treet, til den mest venstreliggende noden på nederste nivå er funnet. Denne returneres.

Metoden nestePostorden sjekker først om startnoden er rotnoden. Hvis ikke, sjekkes det om noden har et høyre søsken eller ikke. Hvis ikke returneres foreldrenoden. Hvis den har det, kalles førstePostorden-metoden på søskenet til noden, og resultatet av dette kallet returneres.

### Oppgave 4

Den iterative metoden kaller først førstePostorden-metoden på rotnoden. Så kalles nestePostorden til vi har nådd siste node i postorden, og for hver node kalles oppgave.utførOppgave på noden.

I den rekursive noden beveger man seg nedover treet ved å prøve å kalle postordenRecursive på først det venstre barnet (hvis det finnes) og så det høyre barnet (hvis det finnes). Så kalles oppgave-utførOppgave på noden selv.

### Oppgave 5

Deserialiseringen gjøres ved at rot-noden legges inn i en Kø. Deretter brukes en while-løkke til å :

* Ta ut en Node.
* Legge til verdien av denne i en ArrayListe.
* Legge til eventuelle barn (venstre, så høyre)

Dette fortsetter til Køen er tom. (Da vil også alle Noder være lagt til i ArrayListen) Så returneres ArrayListen.

Serialiseringen gjøres ved å opprette et nytt tre. Så itereres det gjennom alle elementene i input-arrayet, og brukker leggTil-metoden for å legge de til.

### Oppgave 6

Metoden fjern(T verdi) og fjernAlle(T verdi) løses begge ved en hjelpemetode fjernNode(Node<T> fjernetNode). Denne metoden fjerner en node, og returnerer noden som tar dens plass. I fjern() brukes først hjelpemetoden finnNeste(Node<T> startNode, T verdi) til å finne noden som skal fjernes, og så brukes fjernNode() til å fjerne den.

I fjernAlle() gjøres omtrentlig det samme, men her brukes en while-løkke til å iterere til det ikke lenger er noder i treet med den aktuelle verdien.

I nullstill() brukes samme fremgangsmåte som i oppgave 5; først legges rot-noden i en kø. Deretter går en while-løkke til Køen er tom. For hver node legges eventuelle barn til i køen, før alle pekere i noden nulles ut.
Til slutt settes "antall" til '0', og "rot" til 'null'.