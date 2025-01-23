package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.service.*;
import mr.demonid.web.client.service.filters.LogFilter;
import mr.demonid.web.client.service.filters.ObjectFilter;
import mr.demonid.web.client.service.filters.PersonFilter;
import mr.demonid.web.client.utils.CommandService;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@AllArgsConstructor
@RequestMapping("/api/web-service")
public class AppController {

    private final ObjectService objectService;
    private LoggerService loggerService;
    private WorkScheduleService workScheduleService;
    private PhoneService phoneService;
    private PersonService personService;


    @GetMapping
    public String baseDir() {
        return "/index-page";
    }


    /**
     * Страница по работе с отдельно взятым объектом.
     */
    @GetMapping("/object/detail/{objectId}")
    public String objectDetail(@PathVariable Long objectId, Model model) {
        injectToken(model);

        ObjectEntityDTO object = objectService.getObjectById(objectId);
        model.addAttribute("object", object);
        model.addAttribute("availableSchedules", workScheduleService.getWorkSchedules());
        List<String> userPermissions = IdnUtil.extractScopesFromToken();
        model.addAttribute("userPermissions", userPermissions);
        return "/object-detail";
    }

    /**
     * Страница по работе с БД объектов.
     * @param pageSize    Размер страницы (строк).
     * @param currentPage Текущая страница.
     * @param fromId      Фильтр: начальное значение диапазона искомых номеров объектов (null - выбираются все).
     * @param toId        Фильтр: конечное значение диапазона искомых номеров объектов (null - максимальное).
     */
    @GetMapping("/objects")
    public String objectsList(
            @RequestParam(name = "elemsOfPage", defaultValue = "20") int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
            @RequestParam(name = "fromId", required = false) Long fromId,
            @RequestParam(name = "toId", required = false) Long toId,
            Model model) {

        injectToken(model);

        // Создаем данные по страничной выборке
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id").ascending());
        // Фильтр размера страниц
        model.addAttribute("listPageSizes", List.of(5,10,15, 20, 25, 50, 75, 100, 150, 200));

        // получаем данные по объектам
        ObjectFilter filter = new ObjectFilter(fromId, toId);
        PageDTO<ObjectEntityDTO> page = objectService.getAllObjectsWithFilter(filter, pageable);
        List<ObjectViewDTO> objects = page.getContent().stream().map(ObjectViewDTO::toView).toList();

        // задаем данные об объектах и поля фильтра
        model.addAttribute("objects", objects);
        model.addAttribute("fromId", filter.getFromId());
        model.addAttribute("toId", filter.getToId());
        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);
        // права пользователя
        List<String> userPermissions = IdnUtil.extractScopesFromToken();
        model.addAttribute("userPermissions", userPermissions);
        return "/objects";
    }

    /**
     * Страница по работе с отдельно взятым сотрудником.
     */
    @GetMapping("/person/detail/{tabNo}")
    public String personDetail(@PathVariable Long tabNo, Model model) {
        injectToken(model);

        PersonDTO person = personService.getPersonById(tabNo);
        model.addAttribute("person", person);
        model.addAttribute("availableSchedules", workScheduleService.getWorkSchedules());
        List<String> userPermissions = IdnUtil.extractScopesFromToken();
        model.addAttribute("userPermissions", userPermissions);
        return "/person-detail";
    }

    /**
     * Страница по работе с БД сотрудников.
     * @param pageSize    Размер страницы (строк).
     * @param currentPage Текущая страница.
     * @param tabNo       Фильтр: табельный номер сотрудника (null || "" - выбирается любой).
     * @param lastName    Фильтр: фамилия (null || "" - выбирается любая).
     * @param firstName   Фильтр: имя (null || "" - выбирается любое).
     * @param fromDate    Фильтр: выборка от заданной даты (д/р) (null - выбирается любая).
     * @param toDate      Фильтр: выборка до заданной даты (д/р) (null - текущая, но без fromDate не работает).
     * @param genderType  Фильтр: пол (null || "" - выбираются все).
     */
    @GetMapping("/persons")
    public String personsList(
            @RequestParam(name = "elemsOfPage", defaultValue = "20") int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
            @RequestParam(name = "tabNo", required = false) Long tabNo,
            @RequestParam(name ="lastName", defaultValue = "") String lastName,
            @RequestParam(name ="firstName", defaultValue = "") String firstName,
            @RequestParam(name ="from", required = false) LocalDate fromDate,
            @RequestParam(name ="to", required = false) LocalDate toDate,
            @RequestParam(name ="genderType", defaultValue = "") String genderType,
            Model model) {

        injectToken(model);

        genderType = genderType.isBlank() ? null : genderType;
        // Создаем фильтр
        PersonFilter personFilter = new PersonFilter(
                tabNo,
                lastName.isBlank() ? null : lastName,
                firstName.isBlank() ? null : firstName,
                fromDate,
                toDate,
                genderType == null ? null : GenderType.valueOf(genderType.toUpperCase())
        );
        // Создаем данные по страничной выборке
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("tabNo").ascending());
        // готовим данные для фильтра пола
        List<Integer> listPageSizes = List.of(5,10,15, 20, 25, 50, 75, 100, 150, 200);
        model.addAttribute("listPageSizes", listPageSizes);
        model.addAttribute("genderTypes", GenderType.getAllTypes());

        // делаем выборку данных из БД и заносим их в шаблон
        PageDTO<PersonDTO> page = personService.getAllPersonsWithFilter(personFilter, pageable);
        model.addAttribute("persons", page.getContent());
        model.addAttribute("tabNo", personFilter.getTabNo());
        model.addAttribute("lastName", personFilter.getLastName());
        model.addAttribute("firstName", personFilter.getFirstName());
        model.addAttribute("from", personFilter.getFromDate());
        model.addAttribute("to", personFilter.getToDate());
        model.addAttribute("selectedGenderType", genderType);

        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);
        // права пользователя
        List<String> userPermissions = IdnUtil.extractScopesFromToken();
        model.addAttribute("userPermissions", userPermissions);
        return "/persons";
    }


    /**
     * Страница по работе с БД телефонов.
     * @param number Шаблон для выборки из БД номеров (null || "" - выбираются все)
     * @param type   Тип выбираемых из БД телефонов (null || "" - выбираются все)
     */
    @GetMapping("/phones")
    public String getPhonesWithFilter(
            @RequestParam(name = "phoneNumber", defaultValue = "") String number,
            @RequestParam(name ="phoneType", defaultValue = "") String type,
            Model model) {

        injectToken(model);

        number = number.isBlank() ? null : number;
        type = type.isBlank() ? null : type;

        // передаем список типов в фильтр модели
        model.addAttribute("phoneTypes", PhoneType.getAllTypes());
        model.addAttribute("selectedPhoneType", type);
        model.addAttribute("phoneNumber", number);
        List<PhoneDTO> phones = phoneService.getPhonesWithFilter(number, type);
        model.addAttribute("phones", phones);
        model.addAttribute("userPermissions", IdnUtil.extractScopesFromToken());
        return "/phones";
    }


    /**
     * Страница по работе с БД режимов работы.
     */
    @GetMapping("/work-schedule")
    public String workSchedule(Model model) {
        injectToken(model);
        List<WorkScheduleDTO> schedules = workScheduleService.getWorkSchedules();
        model.addAttribute("workSchedules", schedules);
        List<String> userPermissions = IdnUtil.extractScopesFromToken();
        model.addAttribute("userPermissions", userPermissions);
        return "/work-schedule";
    }


    /**
     * Страница логов протокола обмена данными между оператором и оборудованием.
     * @param pageSize    Размер страницы (строк).
     * @param currentPage Текущая страница.
     * @param repeater    Текущий отображаемый ретранслятор (-1 - все доступные)
     * @param key         Текущий ключ (-1 - все доступные)
     * @param from        Фильтр: от какой даты ищем (по умолчанию - сутки назад от to)
     * @param to          Фильтр: до какой даты ищем (по умолчанию - до текущей)
     */
    @GetMapping("/read-logger")
    public String readLogGetWithFilterPages(
            @RequestParam(name = "elemsOfPage", defaultValue = "20") int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "-1") int repeater,
            @RequestParam(defaultValue = "-1") int key,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            Model model) {

        injectToken(model);
        // Создаем данные по страничной выборке
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("date").descending());
        // Фильтр размера страниц
        model.addAttribute("listPageSizes", List.of(5,10,15, 20, 25, 50, 75, 100, 150, 200));
        // получаем данные по объектам
        LogFilter filter = new LogFilter(repeater, key, from, to);;
        PageDTO<LogOperationDTO> page = loggerService.getLogOperationsWithFilter(filter, pageable);

        // задаем данные для шаблона
        model.addAttribute("operationDescriptions", CommandService.getCommandMap());
        model.addAttribute("repeater", repeater);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        List<LogOperationDTO> items = page.getContent();
        model.addAttribute("items", items);
        model.addAttribute("repeaters", generateArray(1, 8));

        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);
        return "/show-log";
    }


    /*
     * Внедряет текущий Jwt-Токен в шаблон Thymeleaf.
     */
    private void injectToken(Model model) {
        String token = IdnUtil.getCurrentUserToken();
        if (token != null) {
            System.out.println("token: " + token);
            model.addAttribute("jwtToken", token);
        }
    }

    /**
     * Возвращает список значений заданного диапазона.
     */
    public List<Integer> generateArray(int start, int end) {
        if (start > end) {
            int t = start;
            start = end;
            end = t;
        }
        return IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }


    private void generatePersons(int count) {
        String[] names = {"Сергей", "Андрей", "Петр", "Иван", "Сидор", "Еремей", "Афанасий", "Николай", "Василий", "Александр"};
        String[] middle= {"Сергеевич", "Андреевич", "Петрович", "Иванович", "Сидорович", "Еремеевич", "Афанасьевич", "Николаевич", "Васильевич", "Александрович"};
        String[] last  = {"Петров", "Сидоров", "Иванов", "Большаков", "Трифонов", "Востриков", "Малышев", "Песков", "Чердынцев", "Пустовалов", "Арбеков", "Блиновский", "Белов", "Семгин", "Семаков", "Кудрявцев", "Борисов", "Пензин", "Макаров", "Шутов", "Шульгин", "Демин", "Романов"};
        Random rnd = new Random();
        for (int i = 0; i < count; i++) {
            personService.createPerson(new PersonDTO(
                    (long) (i+1),
                    names[rnd.nextInt(names.length)], last[rnd.nextInt(last.length)], middle[rnd.nextInt(middle.length)],
                    LocalDate.of(rnd.nextInt(1960, 1990), rnd.nextInt(12)+1, rnd.nextInt(28)+1),
                    GenderType.MALE,
                    null,null,null,null,null)
            );
        }
    }

}

