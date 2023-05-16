package esercitazione;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Esercizio 1: Creazione di un flusso e sottoscrizione
 * Crea un flusso (Observable) che emetta i numeri da 1 a 5.
 * Sottoscriviti al flusso e stampa ogni numero emesso.
 */
public class Esercizio1 {
    public static void main(String[] args) {
        Observable<Integer> observable = Observable.range(1, 5);

        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> System.out.println("Sottoscrizione avviata"))
                .doOnNext(num -> System.out.println("Numero emesso: " + num))
                .doOnComplete(() -> System.out.println("Emissione completata"))
                .doFinally(() -> System.out.println("Sottoscrizione terminata"))
                .subscribe(System.out::println);

        // Attendi per qualche istante per consentire l'emissione dei numeri
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();
    }
}
