package com.example.chatting.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface {

    SUCCESS(0, "SUCCESS"),

    USER_ALREADY_EXISTS(-1, "USER_ALREADY_EXISTS"),
    USER_SAVED_FAILED(-2, "USER_SAVED_FAILED"),
    NOT_EXIST_USER(-3, "NOT_EXIST_USER"),
    MIS_MATCH_PASSWORD(-4, "MIS_MATCH_PASSWORD"),
    USER_NOT_EXISTS(-5, "USER_NOT_EXISTS"),
    FRIEND_REQUEST_ALREADY_EXISTS(-6, "FRIEND_REQUEST_ALREADY_EXISTS"),
    FRIEND_REQUEST_NOT_FOUND(-7, "FRIEND_REQUEST_NOT_FOUND"),
    USER_CHECKING_FAIL(-8, "USER_CHECKING_FAIL"),
    INVALID_FRIEND_STATUS(-9, "INVALID_FRIEND_STATUS"),
    INVALID_ACTION(-10, "INVALID_ACTION"),
    CHATROOM_NOT_EXISTS(-11, "CHATROOM_NOT_EXISTS"),
    NOT_CHATROOM_MEMBER(-12, "NOT_CHATROOM_MEMBER"),
    NOT_ROOM_OWNER(-13, "NOT_ROOM_OWNER"),

    TOKEN_IS_INVALID(-200, "token is invalid"),
    ACCESS_TOKEN_IS_NOT_EXPIRED(-201, "token is not expired"),
    TOKEN_IS_EXPIRED(-202, "token is expired");

    private final Integer code;
    private final String message;
}
