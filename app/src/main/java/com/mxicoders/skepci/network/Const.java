package com.mxicoders.skepci.network;

public class Const {

    public class ServiceType {

        public static final String BASE_URL = "http://mbdbtechnology.com/projects/skepsi/ws/Webservice/";
        public static final String REGISTER = BASE_URL + "signup_patient";
        public static final String REGISTER2 = BASE_URL + "signup_psychologist";
        public static final String LOGIN = BASE_URL + "login";
        public static final String PAGES = BASE_URL + "pages";
        public static final String LOGOUT = BASE_URL + "logout";
        public static final String FORGET_PASSWORD = BASE_URL + "forgot_password";
        public static final String GET_PROFILE = BASE_URL + "get_profile";
        public static final String EDIT_PROFILE = BASE_URL + "edit_profile";
        public static final String INVITE_PATIENT = BASE_URL + "invite_patient";
        public static final String CHANGE_PASSWORD = BASE_URL + "change_password";
        public static final String GET_STATE = BASE_URL + "get_state";
        public static final String GET_PATIENT_LIST_PSYCHOLOGIST = BASE_URL + "patient_get_by_psychologist";
        public static final String PATIENT_STATUS_CHANGE_PSYCHOLOGIST = BASE_URL + "patient_status_change";
        public static final String GET_CATEGORY_LIST = BASE_URL + "questionnaire_category_select";
        public static final String QUESTINNARIES_CREATE_BY_PSYCHOLOGIST = BASE_URL + "questionnaire_create";
        public static final String QUESTINNARIES_LIST = BASE_URL + "questionnaire_list";
        public static final String QUESTION_TITLE = BASE_URL + "get_question_title";
        public static final String QUESTION_TITLE_ASSIGNED = BASE_URL + "get_question_title_assigned";
        public static final String QUESTION_TITLE_ANSWED = BASE_URL + "get_question_title_answered";
        public static final String INTERVIEW_TASK = BASE_URL + "interview_task";
        public static final String EMOTION_TASK = BASE_URL + "emotions_task";
        public static final String MOOD_TASK = BASE_URL + "mood_task";
        public static final String QUESTIONNARY_TASK = BASE_URL + "question_task";
        public static final String GET_ARTICLE_LIST = BASE_URL + "article_category_select";
        public static final String CREATE_ARTICLE = BASE_URL + "article_create";
        public static final String ARTICLE_LIST = BASE_URL + "article_list";
        public static final String DELETE_ARTICLE = BASE_URL + "article_delete";
        public static final String CREATE_NOTE = BASE_URL + "note_create";
        public static final String NOTE_LIST = BASE_URL + "note_list";
        public static final String DELETE_NOTE = BASE_URL + "note_delete";
        public static final String EDIT_NOTE = BASE_URL + "note_edit";
        public static final String GET_PSYCHOLOGIST_CHAT = BASE_URL + "get_psychologist_chat";
        public static final String GET_PATIENT_CHAT= BASE_URL + "get_patient_chat";
        public static final String SEND_MESSAGE= BASE_URL + "send_msg";
        public static final String GET_PATIENT_CHAT_LIST= BASE_URL + "get_patient_chatlist";
        public static final String GET_ARTICLE_PATIENT = BASE_URL + "get_article_patient";
        public static final String SEND_ARTICLE = BASE_URL + "send_article";
        public static final String CARD_TASK = BASE_URL + "cards_task";
        public static final String SLEEP_TASK = BASE_URL + "patient_sleep_assign";
        public static final String PATIENT_MOOD_RESPONSE = BASE_URL + "task_mood_response";
        public static final String PATIENT_EMOTION_RESPONSE = BASE_URL + "task_emotions_response";
        public static final String COPING_CARD_CREATE = BASE_URL + "cards_create";
        public static final String GET_COPING_CARD = BASE_URL + "get_coping_cards";
        public static final String PATIENT_SLEEP = BASE_URL + "patient_sleep";
        public static final String TODO_LIST = BASE_URL + "to_do_list";
        public static final String DELETE_COPING_CARD = BASE_URL + "coping_card_delete";
        public static final String GET_QUESTION_TITLE = BASE_URL + "question_title";
        public static final String CREATE_RPD = BASE_URL + "rpd_create";
        public static final String GET_RPD_TITLE = BASE_URL + "get_rpd_title";
        public static final String GET_RPD = BASE_URL + "get_rpd";
        public static final String TASK_HISTORY_PATIENT = BASE_URL + "task_history_patient";
        public static final String GET_QUESTION_DETAIL = BASE_URL + "getquestion";
        public static final String QUESTION_RESPONSE = BASE_URL + "question_response";
        public static final String PATIENT_TASK_HISTORY_PSYCHOLOGIST = BASE_URL + "task_history_psychologist";
        public static final String GET_CONCEPTUALIZATION = BASE_URL + "get_conceptualization";
        public static final String EDIT_CONCEPTUALIZATION = BASE_URL + "conceptualization_edit";
        public static final String DELETE_PATIENT_BY_PSYCHOLOGIST = BASE_URL + "delete_patient_by_psychologist";
        public static final String ARCHIEVE_NOTE_BY_PSYCHOLOGIST = BASE_URL + "psycologist_note_archived";
        public static final String PATIENT_ARCHIEVE = BASE_URL + "patient_archived";
        public static final String PATIENT_UNARCHIEVE = BASE_URL + "patient_unarchived";
        public static final String PATIENT_ARCHIEVE_LIST = BASE_URL + "get_patient_archived";
        public static final String NOTE_ARCHIEVE_LIST = BASE_URL + "archived_note_list";
        public static final String FAVOURITE_ARTICLE = BASE_URL + "favourite_article";
        public static final String FAVOURITE_REMOVE_ARTICLE = BASE_URL + "favourite_article_remove";
        public static final String EDIT_PROFILE_PATIENT_PSYCHOLOGIST_SIDE = BASE_URL + "edit_patient_by_psychologist";
        public static final String MOVE_PATIENT_BY_PSYCHOLOGIST_SIDE = BASE_URL + "move_patient_by_psychologist";
        public static final String FAQ_PSYCHOLOGIST = BASE_URL + "faq_psycologist";
        public static final String FAQ_PATIENT = BASE_URL + "faq_patient";
        public static final String GET_PROFILE_PATIENT_PSYCHOLOGIST_SIDE = BASE_URL + "get_list_patient";
        public static final String DELETE_CHAT = BASE_URL + "delete_chat";
        public static final String READ_MESSAGE = BASE_URL + "read_message";
        public static final String TASK_HISTORY_PROTECTION = BASE_URL + "task_history_protection";
        public static final String INACTIVE_PROTECTION = BASE_URL + "inactivity_protection";
        public static final String COPING_CARD_REPORT = BASE_URL + "copying_card_report";
        public static final String NOTE_REPORT = BASE_URL + "note_report";
        public static final String QUESTION_REPORT = BASE_URL + "question_report";
        public static final String CHECK_EXITS_EMAIL = BASE_URL + "check_email_exist";


    }

    //Preference Name
    public static String PREF_NAME = "imagecloudapi";

    //other
    public static final String URL = "url";

    public class ServiceCode{
        public static final int REGISTER = 1;
        public static final int LOGIN = 2;
        public static final int FORGET_PASS = 8;
    }

    public class Params{

        //extra

        public static final String USER_ID = "user_id";
        public static final String LOGINTYPE = "signup_type";


        // registration details activity

        public static final String FIRST_NAME = "name";
        public static final String LAST_NAME = "last_name_initial";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String BIRTH_DATE = "birth_date";
        public static final String GENDER = "gender";
        public static final String CITY = "city";
        public static final String STATE_ID = "state_id";
        public static final String STATE_NAME = "state_name";
        public static final String DEVICE_ID = "device_id";
        public static final String DEVICE_TYPE = "device_type";

        //login parameter
        public static final String USER_NAME = "email";
        public static final String PASSWORDD = "password";
        public static final String USER_TYPE = "user_type";

        //register pschologist
        public static final String CRP_NO = "crp_number";
        public static final String CPF_NO = "cpf_number";
        public static final String ADDRESSS = "address";
        public static final String APROACH = "approach";




        public static final String Authorization = "Authorization";
        public static final String Authorization_value = "delta141forceSEAL8PARA9MARCOSKATJRT";

    }

}
