package mr.demonid.rs232dev.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Сообщения ТС (от оборудования к АРМ) и ТУ (от оператора АРМ к оборудованию)
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TMessage {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime date;

    private int repeater;

    private int key;

    private int code;

    private String desc;

    //region Конструкторы

//    public TMessage(Date date, int rtr, int key, int code, String desc) {
//        set(date, rtr, key, code, desc);
//    }

//    public TMessage(int rtr, int key, int code, String desc) {
//        this(new Date(), rtr, key, code, desc);
//    }


    //endregion

    //region Сеттеры & Геттеры

    //endregion

//    private void set(Date date, int rtr, int key, int code, String desc)
//    {
//        this.date = date;
//        this.rtr = rtr;
//        this.key = key;
//        this.code = code;
//        this.desc = desc;
//    }

    public String getFormattedTime() {
        return String.format("%02d:%02d:%02d", date.getHour(), date.getMinute(), date.getSecond());
    }

    public String encode()
    {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSS'Z'");
//        String msgDate = formatter.format(date);
        return String.format("%s;%d;%d;%d", date.toString(), repeater, key, code);
    }

//    public boolean decode(String message)
//    {
//        AlarmNoticeEvent res;
//        String[] s = message.replaceAll("\\s+", " ").trim().split(";");
//        if (s.length != 4)
//            return false;
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSS'Z'");
//        try {
//            Date nDate = simpleDateFormat.parse(s[0]);
//            int nRtr = Integer.parseInt(s[1]);
//            int nKey = Integer.parseInt(s[2]);
//            int nCode = Integer.parseInt(s[3]);
//            String nDesc = TeleFactory.getTC().getDescription(Integer.parseInt(s[3]));
//            // конвертация прошла успешно, применяем новые данные
//            set(nDate, nRtr, nKey, nCode, nDesc);
//
//        } catch (ParseException | NumberFormatException e) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString()
    {
        return encode();
    }

}
