W moim projekcie zrealizowałem protokół udp.
Utworzona klasa DAS przyjmuje port i numer ktore pobieram z argumetów maina.
Nastepnie na tych wartościach wywołuje metode "run" która rozpoczyna cały protokół.
W zależności od tego czy port jest zajęty lub nie wywołuje metody "master" lub "slave" ,
które odpowiadają trybom działania programu.
Master pobiera wartość, która dzięki zastosowaniu switcha dobiera odpowiedni case, który realizuje wyznaczone cele.
W momencie w którym próbujemy na nowo otworzyć port, który już jest w użyciu,catch chwyta exception i następnie
uruchamia się metoda "slave", która rozpoczyna tryb slave. 
Tryb Slave uruchamia się na losowo dostępnym porcie i wysyła na zajęty port wartość, która chcieliśmy przekazać.
