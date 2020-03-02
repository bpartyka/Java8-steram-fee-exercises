package pl.klolo.workshops.logic;

import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.mock.HoldingMockGenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class WorkShop {
    /**
     * Lista holdingów wczytana z mocka.
     */
    private final List<Holding> holdings;

    private final Predicate<User> isWoman = user -> user.getSex().equals(Sex.WOMAN);
    private Predicate<User> isMan = m -> m.getSex() == Sex.MAN;

    WorkShop() {
        final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
        holdings = holdingMockGenerator.generate();
    }

    /**
     * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
     */
    long getHoldingsWhereAreCompanies() {
        return holdings.stream().filter(x -> x.getCompanies().size() >= 1).count();

    }

    //    /**
//     * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
//     */
    List<String> getHoldingNames() {
        return holdings.stream()
                .map(x -> x.getName().toLowerCase())
                .collect(toList());
    }

    //
//    /**
//     * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane.
//     * String ma postać: (Coca-Cola, Nestle, Pepsico)
//     */
    String getHoldingNamesAsString() {
        return holdings.stream().map(x -> x.getName()).sorted()
                .collect(toList())
                .toString().replace("[", "(")
                .replace("]", ")");

    }

    //    /**
//     * Zwraca liczbę firm we wszystkich holdingach.
//     */
    long getCompaniesAmount() {

        return holdings.stream()
                .mapToInt(x -> x.getCompanies().size())
                .sum();
    }

    //
//    /**
//     * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
//     */
    long getAllUserAmount() {
        return holdings.stream()
                .flatMap(x->x.getCompanies().stream())
                .flatMap(y->y.getUsers().stream())
                .count();



    }
//
//    /**
//     * Zwraca listę wszystkich nazw firm w formie listy.
//     Tworzenie strumienia firm umieść w osobnej metodzie którą
//     * później będziesz wykorzystywać.
//     */
    List<String> getAllCompaniesNames() {
        return holdings.stream().flatMap(x->x.getCompanies().stream())
                .map(y->y.getName()).collect(toList());
    }
//
//    /**
//     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList.
//     Obiektów nie przepisujemy
//     * po zakończeniu działania strumienia.
//     */
    LinkedList<String> getAllCompaniesNamesAsLinkedList() {

        return holdings.stream().flatMap(x->x.getCompanies().stream())
                .map(y->y.getName())
                .collect(Collectors.toCollection(LinkedList::new));

    }
//
//    /**
//     * Zwraca listę firm jako String gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
//     */
    String getAllCompaniesNamesAsString() {
return  holdings.stream().flatMap(x->x.getCompanies().stream())
        .map(y->y.getName())
        .collect(Collectors.joining("+"));
    }
//
//    /**
//     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+".
//     * Używamy collect i StringBuilder.
//     * <p>
//     * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
//     */
//    String getAllCompaniesNamesAsStringUsingStringBuilder() {
//            holdings.stream().flatMap(z->z.getCompanies().stream())
//                    .collect(Collector.of(StringBuilder::new, ))
//
//    }

//    /**
//     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
//     */
    long getAllUserAccountsAmount() {
      return holdings.stream().flatMap(x->x.getCompanies().stream())
              .flatMap(y->y.getUsers().stream())
              .mapToInt(z->z.getAccounts().size()).sum();
    }
//
//    /**
//     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości
//     * występują bez powtórzeń i są posortowane.
//     */
    String getAllCurrencies() {
            return holdings.stream().flatMap(x->x.getCompanies().stream())
            .flatMap(y->y.getUsers().stream())
            .flatMap(z->z.getAccounts().stream())
            .map(u->u.getCurrency())
                    .map(xx-> Objects.toString( xx, null))
                            .sorted().distinct().collect(Collectors.joining(", "));
    }
//
//    /**
//     * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze
//     nie uruchamiaj metody
//     * stream, tylko skorzystaj z  Stream.generate. Wspólny kod wynieś do osobnej metody.
//     *
//     * @see #getAllCurrencies()
//     */
List<String> getAllCurrenciesLista() {
    return holdings.stream().flatMap(x -> x.getCompanies().stream())
            .flatMap(y -> y.getUsers().stream())
            .flatMap(z -> z.getAccounts().stream())
            .map(u -> u.getCurrency()).map(xx->Objects.toString(xx, null))
            .collect(toList());


}
    String getAllCurrenciesUsingGenerate() {
        List<String> list = getAllCurrenciesLista();
        return Stream.generate(list.iterator()::next)
                .limit(list.size())
                .sorted().distinct()
                .collect(joining(", "));


    }
//

    /**
     * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment
     * kodu tworzący strumień użytkowników umieść
     * w osobnej metodzie. Predicate określający czy mamy do czynienia
     * z kobietą niech będzie polem statycznym w klasie.
     */
    long getWomanAmount() {
        return holdings.stream().flatMap(x->x.getCompanies().stream())
                .flatMap(y-> y.getUsers().stream())
                .filter(z->z.getSex().equals(Sex.WOMAN))
                .count();
    }
//
//
    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency.
     */
    BigDecimal getAccountAmountInPLN(final Account account) {
        return account.getAmount()
                .multiply(valueOf(account.getCurrency().rate))
                .round(new MathContext(4, RoundingMode.HALF_UP));



    }

    /**
     * Przelicza kwotę na podanych rachunkach na złotówki
     * za pomocą kursu określonego w enum Currency i sumuje ją.
     */
    BigDecimal getTotalCashInPLN(final List<Account> accounts) {
        return accounts.stream()
                .map(z->z.getAmount().multiply(valueOf(z.getCurrency().rate)))
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);




    }
//
    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
     */
    Set<String> getUsersForPredicate(final Predicate<User> userPredicate) {

        return  holdings.stream().flatMap(x->x.getCompanies().stream())
                .flatMap(y->y.getUsers().stream())
                .filter(userPredicate)
                .map(zz->zz.getFirstName())
                .collect(Collectors.toSet());

    }
//
    /**
     * Metoda filtruje użytkowników starszych niż podany jako parametr wiek,
     * wyświetla ich na konsoli, odrzuca mężczyzn
     * i zwraca ich imiona w formie listy.
     */
    List<String> getOldWoman(final int age) {
            return holdings.stream().flatMap(x->x.getCompanies().stream())
                    .flatMap(y->y.getUsers().stream())
                    .filter(w->w.getAge()> age )
                    .peek(System.out::println)
                    .filter(y->y.getSex().equals(Sex.MAN))
                    .map(y->y.getFirstName())
                    .collect(toList());

    }
//
    /**
     * Dla każdej firmy uruchamia przekazaną metodę.
     */
    void executeForEachCompany(final Consumer<Company> consumer) {
        holdings.stream().flatMap(x->x.getCompanies().stream())
            .forEach(consumer);
    }

    /**
     * Wyszukuje najbogatsza kobietę i zwraca ją. Metoda musi uzwględniać to że rachunki są w różnych walutach.
     */
    //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/55052733/9360524
    Optional<User> getRichestWoman() {

    }

    private BigDecimal getUserAmountInPLN(final User user) {
        return user.getAccounts()
                .stream()
                .map(this::getAccountAmountInPLN)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
     */
    Set<String> getFirstNCompany(final int n) {
            return  holdings.stream().flatMap(x->x.getCompanies().stream())
                    .limit(n)
                    .map(y->y.getName()).collect(Collectors.toSet());
    }
//
    /**
     * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy.
     * Stwórz pomocniczą metodę getAccountStream.
     * Jeżeli nie udało się znaleźć najpopularniejszego rachunku
     * metoda ma wyrzucić wyjątek IllegalStateException.
     * Pierwsza instrukcja metody to return.
     */
    List<Account> getAccountStream () {
        return holdings.stream().flatMap(x->x.getCompanies().stream())
                .flatMap(x->x.getUsers().stream())
                .flatMap(y->y.getAccounts().stream())
                .collect(Collectors.toList());
    }
    AccountType getMostPopularAccountType() {
        List<Account> list = getAccountStream();
        return list.stream()
                .map(x->x.getType())
                .sorted().findFirst().get();
    }

    /**
     * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca
     * wyjątek IllegalArgumentException.
     */
    User getUser(final Predicate<User> predicate) {
        return getUserStream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
     */
    Map<String, List<User>> getUserPerCompany() {
        return getCompanyStream()
                .collect(toMap(Company::getName, Company::getUsers));
    }


    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako String
     * składający się z imienia i nazwiska. Podpowiedź:  Możesz skorzystać z metody entrySet.
     */
    Map<String, List<String>> getUserPerCompanyAsString() {
        BiFunction<String, String, String> joinNameAndLastName = (x, y) -> x + " " + y;
        return getCompanyStream().collect(Collectors.toMap(
                Company::getName,
                c -> c.getUsers()
                        .stream()
                        .map(u -> joinNameAndLastName.apply(u.getFirstName(), u.getLastName()))
                        .collect(Collectors.toList())
        ));
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty
     * typu T, tworzonych za pomocą przekazanej funkcji.
     */
    //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/54969615/9360524
    <T> Map<String, List<T>> getUserPerCompany(final Function<User, T> converter) {
        return getCompanyStream()
                .collect(Collectors.toMap(
                        Company::getName,
                        c -> c.getUsers()
                                .stream()
                                .map(converter)
                                .collect(Collectors.toList())
                ));
    }

    /**
     * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą.
     * Osoby "innej" płci mają zostać zignorowane. Wartością jest natomiast zbiór nazwisk tych osób.
     */
    Map<Boolean, Set<String>> getUserBySex() {
        Predicate<User> isManOrWoman = m -> m.getSex() == Sex.MAN || m.getSex() == Sex.WOMAN;
        return getUserStream()
                .filter(isManOrWoman)
                .collect(partitioningBy(isMan, mapping(User::getLastName, toSet())));
    }

    /**
     * Zwraca mapę rachunków, gdzie kluczem jest numer rachunku, a wartością ten rachunek.
     */
    Map<String, Account> createAccountsMap() {
        return getAccoutStream().collect(Collectors.toMap(Account::getNumber, account -> account));
    }

    /**
     * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
     */
    String getUserNames() {
        return getUserStream()
                .map(User::getFirstName)
                .distinct()
                .sorted()
                .collect(Collectors.joining(" "));
    }

    /**
     * Zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
     */
    Set<User> getUsers() {
        return getUserStream()
                .limit(10)
                .collect(Collectors.toSet());
    }

    /**
     * Zapisuje listę numerów rachunków w pliku na dysku, gdzie w każda linijka wygląda następująco:
     * NUMER_RACHUNKU|KWOTA|WALUTA
     * <p>
     * Skorzystaj z strumieni i try-resources.
     */
    void saveAccountsInFile(final String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            Files.write(Paths.get(String.valueOf(lines)), (Iterable<String>) getAccoutStream()
                    .map(account -> account.getNumber() + "|" + account.getAmount() + "|" + account.getCurrency())
                    ::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca użytkownika, który spełnia podany warunek.
     */
    Optional<User> findUser(final Predicate<User> userPredicate) {
        return getUserStream()
                .filter(userPredicate)
                .findAny();
    }

    /**
     * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie:
     * IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak użytkownika.
     * <p>
     * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów.
     */
    String getAdultantStatus(final Optional<User> user) {
        return user.flatMap(u -> getUserStream().filter(u2 -> Objects.equals(u2, u)).findFirst())
                .map(u -> format("%s %s ma lat %d", u.getFirstName(), u.getLastName(), u.getAge()))
                .orElse("Brak użytkownika");
    }

    /**
     * Metoda wypisuje na ekranie wszystkich użytkowników (imię, nazwisko) posortowanych od z do a.
     * Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred Pasibrzuch, Adam Wojcik
     */
    void showAllUser() {
    }

    /**
     * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu
     * przeliczona na złotówki.
     */
    //TODO: fix
    // java.lang.AssertionError:
    // Expected :87461.4992
    // Actual   :87461.3999
    Map<AccountType, BigDecimal> getMoneyOnAccounts() {
        return getAccoutStream()
                .collect(Collectors.toMap(Account::getType, account -> account.getAmount()
                        .multiply(BigDecimal.valueOf(account.getCurrency().rate))
                        .round(new MathContext(6, RoundingMode.DOWN)), BigDecimal::add));
    }

    /**
     * Zwraca sumę kwadratów wieków wszystkich użytkowników.
     */
    int getAgeSquaresSum() {
    }

    /**
     * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się
     * powtarzać, wszystkie zmienną muszą być final. Jeżeli podano liczbę większą niż liczba użytkowników należy
     * wyrzucić wyjątek (bez zmiany sygnatury metody).
     */
    List<User> getRandomUsers(final int n) {
    }

    /**
     * Zwraca strumień wszystkich firm.
     */
    private Stream<Company> getCompanyStream() {
    }

    /**
     * Zwraca zbiór walut w jakich są rachunki.
     */
    private Set<Currency> getCurenciesSet() {
    }

    /**
     * Tworzy strumień rachunków.
     */
    private Stream<Account> getAccoutStream() {
    }

    /**
     * Tworzy strumień użytkowników.
     */
    private Stream<User> getUserStream() {
    }

    /**
     * 38.
     * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem
     * jest obiekt User a wartością suma pieniędzy na rachunku danego typu przeliczona na złotkówki.
     */
    //TODO: zamiast Map<Stream<AccountType>, Map<User, BigDecimal>> metoda ma zwracać
    // Map<AccountType>, Map<User, BigDecimal>>, zweryfikować działania metody
    Map<Stream<AccountType>, Map<User, BigDecimal>> getMapWithAccountTypeKeyAndSumMoneyForManInPLN() {
    }

    private Map<User, BigDecimal> manWithSumMoneyOnAccounts(final Company company) {
    }

    private BigDecimal getSumUserAmountInPLN(final User user) {
    }

    /**
     * 39. Policz ile pieniędzy w złotówkach jest na kontach osób które nie są ani kobietą ani mężczyzną.
     */
    BigDecimal getSumMoneyOnAccountsForPeopleOtherInPLN() {
    }

    /**
     * 40. Wymyśl treść polecenia i je zaimplementuj.
     * Policz ile osób pełnoletnich posiada rachunek oraz ile osób niepełnoletnich posiada rachunek. Zwróć mapę
     * przyjmując klucz True dla osób pełnoletnich i klucz False dla osób niepełnoletnich. Osoba pełnoletnia to osoba
     * która ma więcej lub równo 18 lat
     */
    Map<Boolean, Long> divideIntoAdultsAndNonAdults() {
    }
}
