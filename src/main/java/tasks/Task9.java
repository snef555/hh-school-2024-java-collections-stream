package tasks;

import common.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    /*
    IDE не нравится проверка на size, предлагает заменить на isEmpty
    в целом реализация внутри не ясна, но кажется обращение к size() не должно в реальности вызывать проход по списку
    для подсчета кол-ва элементов, т.к. логично хранить размер во внутренней переменной
    */
    if (persons.isEmpty()) {
      return Collections.emptyList();
    }
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
    String result = "";
    if (person.secondName() != null) {
      result += person.secondName();
    }

    //если строка будет пустой, то лишний пробел в ФИО
    if (person.firstName() != null) {
      result += " " + person.firstName();
    }

    //отчество -> middleName + если строка будет пустой, то лишний пробел в ФИО (для имени добавлять не стал, т.к. имя есть всегда)
    if (person.middleName() != null && ! person.middleName().isEmpty()) {
      result += " " + person.middleName();
    }
    return result;
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    Map<Integer, String> map = new HashMap<>(1);
    /*
    Если передана пустая коллекция возвращаем HashMap c capacity 1
    Если передана не пустая коллекция, то map можно получить через stream
    */
    if (! persons.isEmpty()) {
      map = persons.stream().collect(Collectors.toMap(Person::id, this::convertPersonToString, (a, b) -> a));
    }
    /*
    for (Person person : persons) {
      if (!map.containsKey(person.id())) {
        map.put(person.id(), convertPersonToString(person));
      }
    }*/

    return map;
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    boolean has; /* = false; */
    /*
    надо проверить есть ли пересечение у этих множеств, если есть то совпадение есть
     */
    persons1.retainAll(persons2);
    has = ! persons1.isEmpty();
    /*
    for (Person person1 : persons1) {
      for (Person person2 : persons2) {
        if (person1.equals(person2)) {
          has = true;
        }
      }
    }
    */
    return has;
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    count = 0;
    /*
    оптимизация, за один проход можно посчитать кол-во четных чисел
    я хотел сделать переменную count локальной, но дальше началась какая-то жесть от IDE
    long предлагает заменить на AtomicLong, дальше завернуть в объект или массив, в общем отказался от этой идеи :)
    */
    numbers.forEach(num -> {if (num % 2 == 0) count++;});
    //numbers.filter(num -> num % 2 == 0).forEach(num -> count++);
    return count;
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
