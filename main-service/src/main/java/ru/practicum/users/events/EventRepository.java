package ru.practicum.users.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.users.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event as e " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate between ?4 and ?5 " +
            "order by e.id desc ")
    List<Event> findAllByInitiatorIdAndStateAndCategories(List<Integer> initiatorIds, List<String> states,
                                                          List<Integer> categories, LocalDateTime rangeStart,
                                                          LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long Id);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndAvailable(LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsPaid(LocalDateTime now,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsAvailable(LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate > ?1 " +
            "order by e.views desc")
    List<Event> getEventsAll(LocalDateTime now,
                             Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndAvailableByDate(LocalDateTime now,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidByDate(LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableByDate(LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate > ?1 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllByDate(LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsWitchCatAndPaidAndAvailable(List<Integer> categories,
                                                     LocalDateTime now,
                                                     Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndCat(List<Integer> categories,
                                    LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsAvailableAndCat(List<Integer> categories,
                                         LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsAllAndCat(List<Integer> categories,
                                   LocalDateTime now,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsWitchCatAndPaidAndAvailableByDate(List<Integer> categories,
                                                           LocalDateTime now,
                                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndCatByDate(List<Integer> categories,
                                          LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableAndCatByDate(List<Integer> categories,
                                               LocalDateTime now,
                                               Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllAndCatByDate(List<Integer> categories,
                                         LocalDateTime now,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailableAndPaid(String text,
                                                 LocalDateTime now,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailable(String text,
                                          LocalDateTime now,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsTextAndPaid(String text,
                                     LocalDateTime now,
                                     Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate > ?2 " +
            "order by e.views desc")
    List<Event> getEventsText(String text,
                              LocalDateTime now,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableAndPaidByDate(String text,
                                                       LocalDateTime now,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableByDate(String text,
                                                LocalDateTime now,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndPaidByDate(String text,
                                           LocalDateTime now,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate > ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextByDate(String text,
                                    LocalDateTime now,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaid(String text,
                                                              List<Integer> categories,
                                                              LocalDateTime now,
                                                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailable(String text,
                                                       List<Integer> categories,
                                                       LocalDateTime now,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndPaid(String text,
                                                  List<Integer> categories,
                                                  LocalDateTime times,
                                                  Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategories(String text,
                                           List<Integer> categories,
                                           LocalDateTime times,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaidByDate(String text,
                                                                    List<Integer> categories,
                                                                    LocalDateTime now,
                                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableByDate(String text,
                                                             List<Integer> categories,
                                                             LocalDateTime now,
                                                             Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndPaidByDate(String text,
                                                        List<Integer> categories,
                                                        LocalDateTime now,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate > ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesByDate(String text,
                                                 List<Integer> categories,
                                                 LocalDateTime now,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndAvailable(LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsPaid(LocalDateTime start,
                              LocalDateTime end,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsAvailable(LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between ?1 and ?2 " +
            "order by e.views desc")
    List<Event> getEventsAll(LocalDateTime start,
                             LocalDateTime end,
                             Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndAvailableByDate(LocalDateTime start,
                                                LocalDateTime end,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidByDate(LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableByDate(LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between ?1 and ?2 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllByDate(LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsWitchCatAndPaidAndAvailable(List<Integer> categories,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsPaidAndCat(List<Integer> categories,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsAvailableAndCat(List<Integer> categories,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsAllAndCat(List<Integer> categories,
                                   LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsWitchCatAndPaidAndAvailableByDate(List<Integer> categories,
                                                           LocalDateTime start,
                                                           LocalDateTime end,
                                                           Pageable pageable);

    @Query("select e from Event e " +
            "where e.paid = true " +
            "and e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsPaidAndCatByDate(List<Integer> categories,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsAvailableAndCatByDate(List<Integer> categories,
                                               LocalDateTime start,
                                               LocalDateTime end,
                                               Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsAllAndCatByDate(List<Integer> categories,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailableAndPaid(String text,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndAvailable(String text,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsTextAndPaid(String text,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.views desc")
    List<Event> getEventsText(String text,
                              LocalDateTime start,
                              LocalDateTime end,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableAndPaidByDate(String text,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndAvailableByDate(String text,
                                                LocalDateTime start,
                                                LocalDateTime end,
                                                Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.paid = true " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndPaidByDate(String text,
                                           LocalDateTime start,
                                           LocalDateTime end,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.eventDate between ?2 and ?3 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextByDate(String text,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaid(String text,
                                                              List<Integer> categories,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndAvailable(String text,
                                                       List<Integer> categories,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategoriesAndPaid(String text,
                                                  List<Integer> categories,
                                                  LocalDateTime start,
                                                  LocalDateTime end,
                                                  Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.views desc")
    List<Event> getEventsTextAndCategories(String text,
                                           List<Integer> categories,
                                           LocalDateTime start,
                                           LocalDateTime end,
                                           Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.paid = true " +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableAndPaidByDate(String text,
                                                                    List<Integer> categories,
                                                                    LocalDateTime start,
                                                                    LocalDateTime end,
                                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2" +
            "and e.confirmedRequests < e.participantLimit " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndAvailableByDate(String text,
                                                             List<Integer> categories,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.paid = true " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesAndPaidByDate(String text,
                                                        List<Integer> categories,
                                                        LocalDateTime start,
                                                        LocalDateTime end,
                                                        Pageable pageable);

    @Query("select e from Event e " +
            "where (lower(e.annotation) like ?1 " +
            "or lower(e.description) like ?1) " +
            "and e.category.id in ?2 " +
            "and e.eventDate between ?3 and ?4 " +
            "order by e.eventDate desc")
    List<Event> getEventsTextAndCategoriesByDate(String text,
                                                 List<Integer> categories,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 Pageable pageable);
}
