package esercitazione;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Esercizio 3: Unione di più flussi
 * Crea due flussi separati che emettono i numeri da 1 a 3.
 * Unisci i due flussi in un unico flusso.
 * Sottoscriviti al flusso unito e stampa ogni numero emesso.
 */
public class Esercizio3 {
    public static void main(String[] args) {
        // Creazione dei due flussi separati
        Observable<Integer> observable1 = Observable.range(1, 3)
                .subscribeOn(Schedulers.computation()) // Esecuzione asincrona su un thread di calcolo
                .map(num -> num * 2); // Trasformazione: moltiplicazione per 2

        Observable<Integer> observable2 = Observable.range(4, 3)
                .subscribeOn(Schedulers.computation()) // Esecuzione asincrona su un thread di calcolo
                .map(num -> num * 3); // Trasformazione: moltiplicazione per 3

        // Unione dei due flussi in un unico flusso
        Disposable disposable = Observable.merge(observable1, observable2)
                .observeOn(Schedulers.newThread()) // Osservatore eseguito su un nuovo thread
                .subscribe(Esercizio3::processNumber); // Gestione personalizzata di ogni numero emesso

        // Attendi per qualche istante per consentire l'emissione dei numeri
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interruzione della sottoscrizione e liberazione delle risorse
        disposable.dispose();
    }

    private static void processNumber(int number) {
        System.out.println("Numero elaborato: " + number + " (Thread: " + Thread.currentThread().getName() + ")");
    }
}
