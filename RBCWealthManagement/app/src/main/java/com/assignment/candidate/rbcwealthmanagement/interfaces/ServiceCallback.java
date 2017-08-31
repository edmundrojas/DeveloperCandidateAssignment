package com.assignment.candidate.rbcwealthmanagement.interfaces;

import com.assignment.candidate.rbcwealthmanagement.models.ErrorResponse;

public interface ServiceCallback<T> {

    void onSuccess(T successResponse);

    void onFailure(ErrorResponse errorResponse);

    void onAuthenticationRequired();
}