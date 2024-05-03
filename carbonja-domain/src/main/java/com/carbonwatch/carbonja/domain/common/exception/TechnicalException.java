
package com.carbonwatch.carbonja.domain.common.exception;

/**
 * Technical exception used to mask root exception to the caller.
 */
public class TechnicalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TechnicalException() {
        super();
    }

    public TechnicalException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TechnicalException(final String message) {
        super(message);
    }

    public TechnicalException(final Throwable cause) {
        super(cause);
    }

}
