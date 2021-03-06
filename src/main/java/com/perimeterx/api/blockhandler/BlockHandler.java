package com.perimeterx.api.blockhandler;

import com.perimeterx.models.PXContext;
import com.perimeterx.models.configuration.PXConfiguration;
import com.perimeterx.models.exceptions.PXException;

import javax.servlet.http.HttpServletResponseWrapper;

/**
 * BlockHandler is a common interface to be applied on block event
 * <p>
 * Created by Shikloshi on 03/07/2016.
 */
public interface BlockHandler {

    /**
     * Blocking handle will be called when pxVerify will return that user is not verified
     *
     * @param context         - requests context
     * @param responseWrapper - response wrapper
     * @throws PXException
     */
    void handleBlocking(PXContext context, PXConfiguration pxConfig, HttpServletResponseWrapper responseWrapper) throws PXException;

}
