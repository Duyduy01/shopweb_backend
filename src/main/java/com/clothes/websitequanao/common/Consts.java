package com.clothes.websitequanao.common;

import java.util.HashMap;
import java.util.Map;

public class Consts {

    public static final String SHOP_NAME = "Cửa hàng bán quần áo";

    public class Status {
        public static final String ACTIVE = "ACTIVE";
    }
    public class CommentStatus {
        public static final int CMT_SUCCESS = 1;
        public static final int CMT_INCOMPLETE = 0;
    }
    public class FunctionStatus {
        public static final int ON = 1;
        public static final int OFF = 0;
    }

    public class FactorAuthenType {
        public static final String EMAIL = "EMAIL";
        public static final String APP = "APP";
        public static final String NONE = "NONE";
        public static final String SMS = "SMS";
        public static final String PHONE = "PHONE";
    }

    public class RegexPattern {
        public static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        /**
         * ^                 # start-of-string
         * (?=.*[0-9])       # a digit must occur at least once
         * (?=.*[a-z])       # a lower case letter must occur at least once
         * (?=.*[A-Z])       # an upper case letter must occur at least once
         * (?=.*[@#$%^&+=])  # a special character must occur at least once
         * (?=\S+$)          # no whitespace allowed in the entire string
         * .{8,}             # anything, at least eight places though
         * $                 # end-of-string
         */

        public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        public static final String PHONE_PATTERN = "^[0-9]*$";

    }


    public class SendEmailReason {
        public static final String FOR_REGISTER = "FOR_REGISTER";
        public static final String FOR_LOGIN = "FOR_LOGIN";
        public static final String FOR_RESET_PASSWORD = "FOR_RESET_PASSWORD";
        public static final String FOR_WELCOME = "FOR_WELCOME";

    }


    public class UserType {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
        public static final String MANUFACTURE = "MANUFACTURE";

        public static final String PENDING = "PENDING";
        public static final String ACTIVE = "ACTIVE";

        public static final long USER_ID = 1;
        public static final long ADMIN_ID = 2;
        public static final long MANUFACTURE_ID = 3;

        public static final int USER_ACTIVE = 1;
        public static final int USER_INACTIVE = -1;
    }


    public class SortType {
        public static final String ASC = "ASC";
        public static final String DESC = "DESC";
    }

    public static Map<String, String> MAP_PARAM_SECURITIES_CONFIG = new HashMap();

    public class SystemParamKey {
        public static final String GENERATE_ADDRESS_PASS = "beedu.generate.address.pass";
        public static final String GENERATE_ADDRESS_DIRECTORY = "beedu.generate.address.dir";
        public static final String ETHEREUM_HOST_ADDRESS = "ethereum.host.address";
        public static final String BINANCE_HOST_ADDRESS = "binance.host.address";
        public static final String BITCOIN_IS_TESTNET = "bitcoin.is.testnet";
        public static final String ETHEREUM_PING_ADDRESS = "ping.eth.node.address";
        public static final String BINANCE_PING_ADDRESS = "ping.bnb.node.address";
        public static final String SYSTEM_SPENDER_ADDRESS = "system.spender.address";
        public static final String SYSTEM_BNB_SPENDER_ADDRESS = "system.bnb.spender.address";
        public static final String FIXED_RATE_TO_HOT_WALLET = "system.schedule.fixedrate_to_hotwallet";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE = "system.token.min.crowd.sale";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_BTC = "system.token.min.crowd.sale.with_btc";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_ETH = "system.token.min.crowd.sale.with_eth";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_USDT = "system.token.min.crowd.sale.with_usdt";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_USDC = "system.token.min.crowd.sale.with_usdc";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_BNB = "system.token.min.crowd.sale.with_bnb";
        public static final String SYSTEM_TOKEN_MIN_CROWD_SALE_WITH_BUSD = "system.token.min.crowd.sale.with_busd";
        public static final String SYSTEM_TOKEN_CURRENCY = "system.token.currency";
        public static final String SYSTEM_TOKEN_CURRENCY_NAME = "system.token.currency_name";
        public static final String SYSTEM_TOKEN_CURRENCY_LOGO = "system.token.currency_logo";
        public static final String SYSTEM_TOKEN_CURRENCY_COLOR = "system.token.currency_color";
        public static final String SYSTEM_TOKEN_CURRENCY_CODE_COLOR = "system.token.currency_code_color";
        public static final String SYSTEM_TOKEN_BACKGROUND_COLOR = "system.token.background_color";
        public static final String SYSTEM_TOKEN_CURRENCY_FAVICON = "system.token.currency_favicon";
        public static final String SYSTEM_TOKEN_CURRENCY_SUPPORT_EMAIL = "system.token.support_email";
        public static final String SYSTEM_TOKEN_CURRENCY_MARKETING_EMAIL = "system.token.marketing_email";
        public static final String SYSTEM_TOKEN_CURRENCY_ADMIN_EMAIL = "system.token.admin_email";
        public static final String SYSTEM_TOKEN_APP_ANDROID_DOWNLOAD = "system.token.app.android.download";
        public static final String SYSTEM_TOKEN_APP_IOS_DOWNLOAD = "system.token.app.ios.download";
        public static final String SYSTEM_TOKEN_APP_DOWNLOAD = "system.token.app.download";
        public static final String SYSTEM_TOKEN_TERMS = "system.token.terms";
        public static final String SYSTEM_TOKEN_CURRENCY_SUPPORT_URL = "system.token.support.url";
        public static final String SYSTEM_TOKEN_COMPANY_ADDRESS = "system.token.company.address";
        public static final String SYSTEM_KYC_URL = "system.kyc.url";
        public static final String SYSTEM_WEB_BASE_URL = "system.web.base.url";
        public static final String SYSTEM_WEB_TRANSACTION_DETAILS = "system.web.transaction.details";
        public static final String SYSTEM_API_WEBHOOK_URL_TWILLIO_SMS = "system.api.webhook.url.twillio_sms";
        public static final String SYSTEM_PARAM_VERIFICATION_CODE_EXPIRATION_TIME = "code.verification.expiration.time";
        public static final String SYSTEM_PARAM_RESET_CODE_EXPIRATION_TIME = "code.reset.expiration.time";
        public static final String SYSTEM_PARAM_UNLOCK_CODE_EXPIRATION_TIME = "code.unlock.expiration.time";
        public static final String SYSTEM_PARAM_NUM_OF_WRONG_LOGIN = "num.of.wrong.login";
        public static final String SYSTEM_PARAM_NUM_OF_RESEND_CODE = "num.of.resend.code";
        public static final String SYSTEM_PARAM_URL_RESET_PASS = "url.reset.pass";
        public static final String SYSTEM_PARAM_URL_RESET_PIN = "url.reset.pin";
        public static final String SYSTEM_PARAM_URL_UNLOCK_SUCCESS = "url.unlock.success";
        public static final String SYSTEM_PARAM_URL_AFFILIATION = "url.affiliation";
        public static final String SYSTEM_PARAM_URL_LOGIN = "url.login";
        public static final String KYC_REJECT_REASON_ID_PHOTO_NOT_CLEAR = "kyc_reject_reason_id_photo_not_clear";
        public static final String KYC_REJECT_REASON_ID_IS_EXPIRED = "kyc_reject_reason_id_is_expired";
        public static final String KYC_REJECT_REASON_ID_PHOTO_IS_MODIFIED = "kyc_reject_reason_id_photo_is_modified";
        public static final String KYC_REJECT_REASON_ID_NUMBER_WRONG = "kyc_reject_reason_id_number_wrong";
        public static final String KYC_REJECT_REASON_SELFIE_PHOTO_NOT_CLEAR = "kyc_reject_reason_selfie_photo_not_clear";
        public static final String KYC_REJECT_REASON_SELFIE_PHOTO_NOT_MATCHING = "kyc_reject_reason_selfie_photo_not_matching";
        public static final String KYC_REJECT_REASON_SELFIE_PHOTO_INCORRECT = "kyc_reject_reason_selfie_photo_incorrect";
        public static final String KYC_REJECT_REASON_NATIONALITY_NOT_ALLOW = "kyc_reject_reason_nationality_not_allow";
        public static final String SYSTEM_PARAM_FIREBASE_CONFIG_SERVICE_ACCOUNT = "firebase.config.serviceAccount";
        public static final String SYSTEM_PARAM_FIREBASE_URL = "firebase.database.url";
        public static final String SYSTEM_PARAM_FIREBASE_ACCOUNT = "firebase.service.account.id";
        public static final String SYSTEM_PARAM_FIREBASE_NAME = "firebase.database.name";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_CROWDSALE_TRANSACTIONS = "firebase.database.name.crowdsale_transactions";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_EXCHANGE_RATES = "firebase.database.name.exchange_rates";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_ASSET_NETWORKS = "firebase.database.name.asset_networks";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_CROWDSALES_DASHBOARD = "firebase.database.name.crowdsales_dashboard";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_LAST_USER = "firebase.database.name.last_users";
        public static final String SYSTEM_PARAM_FIREBASE_NAME_GAS_TRACKERS = "firebase.database.name.gas_trackers";
        public static final String SYSTEM_PARAM_FIREBASE_CLIENT_USER_UID = "firebase.client.user.uid";
        public static final String SYSTEM_PARAM_FIREBASE_CLIENT_USER_PASS = "firebase.client.user.pass";
        public static final String SYSTEM_PARAM_BONUS_IS_DISPLAY = "system.bonus.display";
        public static final String SYSTEM_PARAM_MAX_NUM_OF_RETRY = "max.num.of.retry.eth";
        public static final String ETHEREUM_API_SCAN_URL = "eth.scan.api.url";
        public static final String ETHEREUM_API_SCAN_KEY = "eth.scan.api.key";
        public static final String BINANCE_API_SCAN_URL = "bnb.scan.api.url";
        public static final String BINANCE_API_SCAN_KEY = "bnb.scan.api.key";
        public static final String URL_API_SCAN_TXLIST = "url.scan.api.txlist";
        public static final String URL_API_SCAN_TXTOKEN = "url.scan.api.txtoken";
        public static final String URL_API_SCAN_TXRECEIPT_STATUS = "url.scan.api.txreceipt.status";
        public static final String URL_API_SCAN_BLOCK = "url.scan.api.block";
        public static final String URL_API_SCAN_TXRECEIPT = "url.scan.api.txreceipt";
        public static final String URL_API_SCAN_GAS = "url.scan.api.gas.tracker";
        public static final String EMAIL_RECEIVE_WARNING_BALANCE_HOT_WALLET = "email.receive.warning.balance.hot.wallet";
        public static final String SYSTEM_PARAM_SEGMENT_API_KEY = "segment.api.key";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_MARKETING = "slack.channel.marketing";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_SUPPORT = "slack.channel.support";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_ACCOUNT = "slack.channel.account";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_SALES = "slack.channel.sales";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_ALERT = "slack.channel.alert";
        public static final String SYSTEM_PARAM_SLACK_CHANNEL_WALLET = "slack.channel.wallet";
        public static final String SYSTEM_PARAM_AWS_BUCKET_NAME = "aws.bucket.name";
        public static final String SYSTEM_PARAM_AWS_KEY_ACCESS = "aws.key.access";
        public static final String SYSTEM_PARAM_AWS_KEY_SECRET = "aws.key.secret";
        public static final String SYSTEM_PARAM_AWS_REGION_NAME = "aws.region.name";
        public static final String SYSTEM_PARAM_AWS_ENDPOINT = "aws.endpoint";
        public static final String SYSTEM_PARAM_STRINGEE_ACCOUNTSID = "stringee.accountSid";
        public static final String SYSTEM_PARAM_STRINGEE_AUTHTOKEN = "stringee.authToken";
        public static final String SYSTEM_PARAM_STRINGEE_VOICECALL_FROMNUMBER = "stringee.voiceCall.fromNumber";
        public static final String SYSTEM_PARAM_STRINGEE_VOICECALL_AUTHTOKEN = "stringee.voiceCall.authToken";
        public static final String SYSTEM_PARAM_STRINGEE_VOICECALL_APIURL = "stringee.voiceCall.apiUrl";
        public static final String SYSTEM_PARAM_TWILIO_ACCOUNTSID = "twilio.accountSid";
        public static final String SYSTEM_PARAM_TWILIO_AUTHTOKEN = "twilio.authToken";
        public static final String SYSTEM_PARAM_TWILIO_FROMPHONENUMBER = "twilio.fromPhoneNumber";
        public static final String SYSTEM_PARAM_SENDGRID_MAIL_HOST = "sendgrid.mail.host";
        public static final String SYSTEM_PARAM_SENDGRID_MAIL_PORT = "sendgrid.mail.port";
        public static final String SYSTEM_PARAM_SENDGRID_MAIL_USERNAME = "sendgrid.mail.username";
        public static final String SYSTEM_PARAM_SENDGRID_MAIL_PASSWORD = "sendgrid.mail.password";
        public static final String SYSTEM_PARAM_SENDGRID_MAIL_FROM = "sendgrid.mail.from";
        public static final String SYSTEM_PARAM_GOOGLE_RECAPTCHA_ENDPOINT = "google.recaptcha.endpoint";
        public static final String SYSTEM_PARAM_GOOGLE_RECAPTCHA_SERVER_KEY = "google.recaptcha.serverKey";
        public static final String SYSTEM_PARAM_SETTING_RESET_2FA_URL = "url.settings.reset2fa";
        public static final String SYSTEM_PARAM_SETTING_RESET_2FA_TIME_EXPIRE = "code.settings.reset2fa.expiration.time";
        public static final String SYSTEM_PARAM_SETTING_CHANGE_PHONE_URL = "url.settings.changephone";
        public static final String SYSTEM_PARAM_SETTING_CHANGE_PHONE_TIME_EXPIRE = "code.settings.changephone.expiration.time";
        public static final String SYSTEM_PARAM_AUTO_TEST = "api.auto.test";
        public static final String SYSTEM_PATH_PUBLIC_KEY = "system.path.publicKey";
        public static final String SYSTEM_PATH_PRIVATE_KEY = "system.path.privateKey";
        public static final String SYSTEM_PATH_NETWORK_ADDRESS_CONFIG = "system.path.network.address";
        public static final String SYSTEM_FILE_HOT_WALLET_BTC_PASSWORD = "hot.wallet.btc.password.txt";
        public static final String SYSTEM_FILE_HOT_WALLET_BNB_PASSWORD = "hot.wallet.bnb.password.txt";
        public static final String SYSTEM_FILE_HOT_WALLET_BNB_PRIVATE_KEY = "hot.wallet.bnb.private.key.json";
        public static final String SYSTEM_FILE_HOT_WALLET_ETH_PASSWORD = "hot.wallet.eth.password.txt";
        public static final String SYSTEM_FILE_HOT_WALLET_ETH_PRIVATE_KEY = "hot.wallet.eth.private.key.json";
        public static final String SYSTEM_FILE_SPENDER_BNB_PASSWORD = "system.bnb.spender.password.txt";
        public static final String SYSTEM_FILE_SPENDER_BNB_PRIVATE_KEY = "system.bnb.spender.private.key.json";
        public static final String SYSTEM_FILE_SPENDER_ETH_PASSWORD = "system.eth.spender.password.txt";
        public static final String SYSTEM_FILE_SPENDER_ETH_PRIVATE_KEY = "system.eth.spender.private.key.json";

        public SystemParamKey() {
        }
    }

    public class brandType {
        public static final long NOBRAND = -1L;
    }

    public class productType {
        public static final String NORMAL = "NORMAL";
    }

    public class ReceiptType {
        public static final int INCOMPLETE = -2;
        public static final int CANCEL = -1;
        public static final int CONFIRM = 1;
        public static final int DELIVERY = 2;
        public static final int SUCCESS = 3;
    }

    public class specialityType {
        public static final String COLOR = "màu sắc";
        public static final String COLOR_TYPE = "color";
        public static final String SIZE_TYPE = "size";
    }

    public class BillType {
        public static final int BILL_CANCEL = -2;
        public static final int BILL_CART = -1;
        public static final int BILL_CONFIRM = 1;
        public static final int BILL_CLOSE_PRODUCT = 2;
        public static final int BILL_TRANSPORTED = 3;
        public static final int BILL_DELIVERY = 4;
        public static final int BILL_SUCCESS = 5;
    }

    public class address {
        public static final String urlGetCity = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        public static final String urlWard = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id";
        public static final String urlDistrict = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    }

    public class favoriteType {
        public static final int DISLIKE = 1;
        public static final int LIKE = 2;
    }


    public class campaignType {
        public static final int CAMPAIGN_CANCEL = -1;
        public static final int CAMPAIGN_UNFINISHED = 1; // chưa hoàn thành việc thêm chi tiết chiến dịch
        public static final int CAMPAIGN_WARN = 2;
        public static final int CAMPAIGN_RUN = 3;
        public static final int CAMPAIGN_SUCCESS = 4;

    }


}

