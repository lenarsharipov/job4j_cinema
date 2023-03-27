package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import static org.assertj.core.api.Assertions.*;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository sql2oFilmRepository;
    private final static List<Film> FILMS = List.of(new Film(1, "На солнце, вдоль рядов кукурузы",
                    """
                    История о небе, несбыточной мечте и подвиге,
                    поразившем весь мир. Дамир Юсупов и предположить не мог,
                    что однажды станет настоящим героем… Обыкновенный мальчишка,
                    он грезил полетами и был уверен, что пойдет по стопам своего
                    отца-летчика, но судьба распорядилась иначе. Спустя годы, когда
                    веру потеряли даже самые близкие люди, Дамир все-таки сумел
                    воплотить заветную мечту и стал пилотом гражданской авиации.
                    И только благодаря его мужеству и преданности любимому делу были
                    спасены 233 жизни: в августе 2019 г., когда у самолета отказали
                    оба двигателя, он сумел посадить его на кукурузное поле…
                    """,  2023, 18, 6, 90, 9),
            new Film(2, "Человек-муравей и Оса: Квантомания",
                    """
                    О приключениях бывшего мелкого преступника Скотта Лэнга,
                    ставшего обладателем костюма, позволяющего уменьшаться и
                    увеличиваться в размерах. Вместе с напарницей Хоуп ван Дайн,
                    дочерью известного физика и энтомолога Хэнка Пима, разработавшего
                    костюм человека-муравья, Скотт вновь спасает мир от предстоящей угрозы.
                    """, 2023, 16, 16, 125, 5),
            new Film(3, "Поехавшая",
                    """
                   После череды неурядиц Аня Смолина решает полностью изменить свою жизнь.
                   Она достает запылившийся велосипед, сажает в прицеп свою таксу Капу и
                   отправляется в Магадан – мириться с мамой, с которой они не общались 13 лет.
                   Жительница мегаполиса, Аня не готова к дороге в 10 тысяч километров, стертым
                   в кровь ногам, опасностям и испытаниям. Она поймет это уже в пути. Но не
                   повернет назад. Сможет ли Аня довериться судьбе и справиться с трудностями,
                   которые ей приготовила долгая дорога, полная невероятных встреч и
                   удивительных пейзажей?
                   """, 2023, 9, 16, 106, 11),
            new Film(4, "Праведник",
                    """
                    1942 год. Офицер Красной Армии Николай Киселев получает приказ вывести с
                    оккупированных белорусских земель за линию фронта свыше двухсот евреев —
                    стариков, женщин и детей, чудом избежавших неминуемой жестокой расправы.
                    Этим истощенным, измученным голодом и страхом людям, потерявшим родных и
                    едва не сошедшим с ума от пережитого ужаса, предстоит пройти пешком по
                    лесным тропам сотни километров, чтобы вновь обрести надежду на спасение и
                    веру в будущее.
                    """, 2023, 6, 12, 160, 12),
            new Film(5, "Нюрнберг",
                    """
                    1945 год. В Нюрнберге начинает работу Международный военный трибунал. На
                    суд, который впоследствии назовут «Процессом века», съезжается огромное
                    количество людей со всего мира: город переполнен участниками процесса и
                    журналистами. Среди тех, кто приезжает в Нюрнберг в составе советской
                    делегации — владеющий несколькими иностранными языками капитан Волгин.
                    Он еще молод, но уже прошел войну… Однажды на улице города Игорь встречает
                    юную русскую девушку Лену. Их зарождающееся чувство пройдет через множество
                    испытаний, но любовь — это единственное, что во все времена спасало мир от
                    расчеловечивания.
                    """, 2023, 7, 12, 135, 10),
            new Film(6, "Аватар: Путь воды",
                    """
                    Прошло больше десяти лет с момента событий первого фильма. Новый эпизод
                    фантастической саги «Аватар: Путь воды» начинает повествование о
                    приключениях семьи Салли — Джейка, Нейтири и их детей. Испытания,
                    выпавшие на их долю, битвы, в которых им предстоит сразиться, и трагедии,
                    которые им предстоит пережить — ради спасения друг друга они
                    готовы ко всему.
                    """, 2022, 16, 16, 206, 1),
            new Film(7, "Беспринципные в деревне",
                    """
                    Генералу Хадякову стукнуло 60 лет, и он решает отпраздновать юбилей
                    не на Патриках, а в настоящей русской деревне — сполным погружением,
                    праздничной трапезой, баней, рыбалкой и деревенским спа. Приглашены
                    все любимые герои, но праздник идёт не по плану. Жители Патриков
                    умудряются настроить против себя местных жителей, переругаться между
                    собой и остаться без пропитания.
                    """, 2023, 8, 16, 100, 2),
            new Film(8, "Кот в сапогах: Последнее желание",
                    """
                    Схватки, погони, женщины. Враги, друзья, интриги. Шпага, шпоры,
                    шляпа. И ко всему этому – яркая внешность мачо. Вот вам и набросок
                    к портрету самого обворожительного анимационного героя. Добавим
                    сорок разбойников, смертельные опасности, крутые виражи судьбы и
                    получим новую картину о Коте в сапогах.
                    """, 2022, 12, 16, 104, 8),
            new Film(9, "Чебурашка",
                    """
                    Иногда, чтобы вернуть солнце и улыбки в мир взрослых, нужен один
                    маленький ушастый герой! Мохнатого непоседливого зверька из далекой
                    апельсиновой страны ждут удивительные приключения в тихом приморском
                    городке, где ему предстоит найти себе имя, друзей и дом. Помогать
                    – и мешать! – ему в этом будут нелюдимый старик-садовник, странная
                    тетя-модница и ее капризная внучка, мальчик, который никак не начнет
                    говорить, и его мама, которой приходится несладко, хотя она и варит
                    самый вкусный на свете шоколад. И многие-многие другие, в чью жизнь
                    вместе с ароматом апельсинов вот-вот ворвутся волшебство и приключения!
                    """, 2023, 13, 6, 120, 4),
            new Film(10, "Царевны и таинственная гостья",
                    """
                    Приключения ждут царевен во время празднования Всеволшебного дня.
                    В школе ждут бал: мальчики готовят сюрприз, а царевны примеряют
                    костюмы животных. Но не успевает торжество начаться, как на школу
                    нападает юная волшебница Марфуша, у которой с учебным заведением
                    свои счёты. Застав Кощея врасплох, она превращает его в ворона,
                    а бросившихся ему на выручку царевен — в зверей, костюмы которых
                    они надели. Теперь до конца Всеволшебного дня царевны должны вернуть
                    себе и Кощею настоящий облик, иначе они навсегда останутся животными.
                    Но как это сделать, если теперь они лишены своих волшебных способностей?
                    """, 2023, 1, 0, 72, 3),
            new Film(11, "Эскортница",
                    """
                    Действие фильма происходит в течение одной ночи в Москве и Дубае.
                    Для всех вокруг Кристина организатор выставок современного искусства,
                    на деле же она организует развлечения для богатых клиентов в самых
                    экзотических местах. На нее работают десятки «элитных» девушек со всего
                    мира. Когда-то в прошлом Кристина тоже работала «хостес» на подобных
                    вечеринках, но, будучи очень амбициозной, она несколько лет назад полностью
                    сменила свой имидж и превратилась в «менеджера» экстра-класса. Благодаря
                    своему активному и коммуникабельному характеру, ей удается всегда быть на
                    гребне волны, заводить новые связи, получать очень богатых клиентов. Но у
                    этой работы есть и обратная сторона – постоянное присутствие криминала,
                    полиции и спецслужб в ее жизни. А также периодические срывы и интриги
                    подопечных девушек, пытающихся оторвать кусок пирога побольше.
                    """, 2023, 14, 18, 90, 7),
            new Film(12, "Сергий против нечисти. Шабаш",
                    """
                    Сергию и Кате предстоит сразиться со свирепой фольклорной нечистью —
                    генно-модифицированным Змеем Горынычем, бандой домовых, терроризирующих
                    людей, а также дать отпор самому Вию, который пытается взять власть в
                    свои руки. Они продолжат и личную борьбу: Катя попытается избавить
                    Сергия от очень вредных привычек, а Сергий — растопить сердце Кати.
                    """, 2023, 10, 18, 100, 13),
            new Film(13, "Дети кукурузы",
                    """
                    В небольшом городке штата Небраска практически никогда ничего не происходит.
                    Но все меняется, когда местные жители решают избавиться от кукурузных полей,
                    которые теперь приносят лишь убыток. По необъяснимой причине дети не желают,
                    чтобы поля сожгли, и устраивают настоящую резню. Но кто в действительности
                    стоит за этой кровавой бойней?
                    """, 2020, 17, 18, 93, 6),
            new Film(14, "Стражи времени",
                    """
                    Однажды в семейном поместье четверо сестер обнаруживают проход, ведущий в
                    загадочный мир. Попав туда, и оказавшись в Королевстве Кеохерус, они должны
                    стать предсказанными героями - Хранителям Времени. Чтобы вернуться домой им
                    предстоит столкнуться с темными силами и волшебными созданиями.
                    """, 2023, 12, 6, 99, 14)
    );

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class
                                        .getClassLoader()
                                        .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    @Test
    public void whenFindAllThenListsEqual() {
        assertThat(sql2oFilmRepository.findAll()).isEqualTo(FILMS);
    }

    @Test
    public void whenFindAllThenListAllFilms() {
        assertThat(sql2oFilmRepository.findById(1).get()).isEqualTo(FILMS.get(0));
        assertThat(sql2oFilmRepository.findById(2).get()).isEqualTo(FILMS.get(1));
        assertThat(sql2oFilmRepository.findById(3).get()).isEqualTo(FILMS.get(2));
        assertThat(sql2oFilmRepository.findById(4).get()).isEqualTo(FILMS.get(3));
        assertThat(sql2oFilmRepository.findById(5).get()).isEqualTo(FILMS.get(4));
        assertThat(sql2oFilmRepository.findById(6).get()).isEqualTo(FILMS.get(5));
        assertThat(sql2oFilmRepository.findById(7).get()).isEqualTo(FILMS.get(6));
        assertThat(sql2oFilmRepository.findById(8).get()).isEqualTo(FILMS.get(7));
        assertThat(sql2oFilmRepository.findById(9).get()).isEqualTo(FILMS.get(8));
        assertThat(sql2oFilmRepository.findById(10).get()).isEqualTo(FILMS.get(9));
        assertThat(sql2oFilmRepository.findById(11).get()).isEqualTo(FILMS.get(10));
        assertThat(sql2oFilmRepository.findById(12).get()).isEqualTo(FILMS.get(11));
        assertThat(sql2oFilmRepository.findById(13).get()).isEqualTo(FILMS.get(12));
        assertThat(sql2oFilmRepository.findById(14).get()).isEqualTo(FILMS.get(13));
    }
}