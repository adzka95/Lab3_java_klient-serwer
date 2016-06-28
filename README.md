# Lab3_java_klient-serwer

polecenie:
wysyłanie pojedynczego pliku i odbieranie współbieżne plików od wielu klientów (1.5 pkt) oraz poprawna aktualizacja kontrolek ProgressBar i Label (1 pkt)
wysyłanie/odbieranie współbieżne wielu plików (1.5 pkt) oraz poprawna aktualizacja kontrolek ProgressBar i Label (1 pkt) 
wskazówka: treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE) umożliwia wybieranie wielu elementów; treeView.getSelectionModel().getSelectedItems() zwraca listę zaznaczonych elementów
wersja rozszerzona dla ambitnych
możliwość kompresji plików w trakcie wysyłania i dekompresji przy odbiorze
(użyć klas java.util.zip.ZipOutputStream i java.util.zip.ZipInputStream)
możliwość szyfrowania plików przed wysłaniem i deszyfrowania przy odbiorze
