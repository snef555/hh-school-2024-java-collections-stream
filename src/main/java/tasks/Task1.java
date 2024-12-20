package tasks;

import common.Person;
import common.PersonService;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
/* Сложность получается O(n) - преобразование Set -> Map (считаем как O(1) + поиск по ключу для каждого элемента O(1)) =>
   для n элементов, получаем сложность O(n)
   Самый плохой вариант - если на входе мы имеем list из одинаковых значений (в жизни мало реально, но в теории может быть),
   в это случае доступ google говорит, что начиная с JAVA 8 операция доступа занимает log (n)
*/
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map<Integer, Person> mapPersons = persons.stream().collect(Collectors.toMap(Person::id, Function.identity(), (a,b) -> a));

    return personIds.stream().map(mapPersons::get).toList();
  }
}
