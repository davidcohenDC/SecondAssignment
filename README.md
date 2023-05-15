PCD Assignment #02
v 1.0-20230421

L’assignment consiste nell'affrontare il problema illustrato nel primo assignment utilizzando le tecniche di programmazione asincrona viste nel corso. In particolare:

Approccio asincrono a Task – Framework Executors
Approccio basato Virtual Threads
Approccio asincrono ad eventi (event-loop) – Framework Vert.x o equivalenti   
Approccio basato su programmazione reattiva – Framework RxJava o equivalenti

Per ogni approccio, si richiede che la soluzione sia organizzata in modo da incapsulare la funzionalità di analisi dei sorgenti in una libreria,  che implementi un'interfaccia SourceAnalyser includa due metodi asincroni (in pseudocodice):

getReport(Directory d)
metodo asincrono che deve restituire un report che include i risultati richiesti dall'analisi (gli N sorgenti con il numero maggiore di linee di codice e la distribuzione complessiva – come indicato nell'assignment 01)

analyzeSources(Directory d)
metodo asincrono che, a differenza del precedente, permetta di effettuare un'analisi incrementale, con produzione incrementale  dei risultati e interrompibile

La progettazione di dettaglio della signature dei metodi – in particolare il tipo di ritorno dei metodi – dipende dall'approccio scelto, ovvero deve sfruttare il più possibile le astrazioni/caratteristiche dell'approccio usato.

Per ogni approccio, considerare due casi:

Programma senza GUI, in cui si usa il metodo getReport per computare e visualizzare in standard output il report
Programma con GUI, in cui si usa il metodo analyzeSources per ottenere incrementalmente i risultati e poter fermare il processo

NOTE

Per ogni approccio, concepire soluzioni che sfruttino il più possibile le caratteristiche paradigmatiche dell'approccio stesso, a livello di progettazione e a livello di implementazione.
Il linguaggio di programmazione di base suggerito è il linguaggio Java, tuttavia per ogni approccio è possibile usare linguaggi di programmazione diversi.


								


