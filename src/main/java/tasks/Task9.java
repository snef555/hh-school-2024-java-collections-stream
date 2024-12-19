package tasks;

import common.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  //private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    /*
    IDE не нравится проверка на size, предлагает заменить на isEmpty
    в целом реализация внутри не ясна, но кажется обращение к size() не должно в реальности вызывать проход по списку
    для подсчета кол-ва элементов, т.к. логично хранить размер во внутренней переменной
    */
    /*
    по remove есть не понятный момент - с т.з. логики операция remove(0) должна выполняться за O(1) т.к. это указатель
    на вершину списка (т.е. при удалении из вершины мы просто указатель начала списка передвигаем на следующий элемент),
    но как оно в действительности реализовано не ясно (например на stackoverflow нашёл такое про ArrayList
    https://ru.stackoverflow.com/questions/722253/
    "При удалении элемента из ArrayList происходит сдвиг всех элементов после индекса удаляемого элемента в массиве на одну позицию левее."
    если это действительно так для ArrayList, то remove(0) слишком дорогостоящая операция, надеюсь это не так :)
    и варианты remove(0) и skip(1) примерно одинаковы по стоимости.
    persons.remove(0);
    return persons.stream().map(Person::firstName).collect(Collectors.toList());
     */
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    //убираем distinct т.к. при преобразовании в Set это будет сделано само
    //+ IDE подсказывает, что в этом случае можно использовать готовый конструктор
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(strName -> strName != null && !strName.isEmpty()).collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toMap(Person::id, this::convertPersonToString, (a, b) -> a));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    /*
    надо проверить есть ли пересечение у этих множеств, если есть то совпадение есть
     */
    Set<Person> samePersons = new HashSet<>(persons1);

    samePersons.retainAll(new HashSet<>(persons2));

    return !samePersons.isEmpty();
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    /*
    Integer.hashCode() = значению числа, которое он хранит.
    HashSet хранит элементы в порядке значений хеш кодов элементов (точнее корзины, но в данном примере у нас
    все элементы разные поэтому использую термин элементы)
    => при вызове toString мы получаем строку с упорядоченными целочисленными элементами.
     */
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
