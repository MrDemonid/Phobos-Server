package mr.demonid.web.client.utils;

import java.util.HashMap;
import java.util.Map;

public class CommandService {

    static Map<Integer, String> map;

    static {
        map = new HashMap<>();
        map.put(125, "Тревога");
        map.put(119, "Взят");
        map.put(114, "Невзят");
        map.put(115, "Снят");
        map.put(109, "Наряд");
        map.put(105, "Подмена УО");
        map.put( 97, "Авария");
        map.put(101, "Направление включено");
        map.put( 99, "Направление выключено");
        map.put(106, "Тип УО");
        map.put(102, "Вскрыт УО");
        map.put(103, "Восстановление УО");
        map.put(104, "Замыкание");
        map.put(120, "Системная");
        map.put(107, "Отказ ретранслятора");
        map.put(108, "Восстановление ретранслятора");
        map.put(98, "Снят");
        map.put(100, "Взят");

        map.put(112, "Взять сразу");
        map.put(110, "Снять");
        map.put(113, "Взять после выхода");
        map.put(122, "Запрос УО");
        map.put(124, "Подключить направление");
        map.put(123, "Отключить направление");
        map.put(111, "Определить тип УО");
        map.put(126, "Запрос взятых");
        map.put(127, "Запрос снятых");
    }


    public static Map<Integer, String> getCommandMap() {
        return map;
    }
}
