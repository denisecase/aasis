package edu.ksu.cis.macr.aasis.simulators;

import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Class that tries to initialize a connection with MatLab.
 *
 *  @author Matt Brown and Denise Case
 */
public class MatLabConnection {
    private static final Logger LOG = LoggerFactory
            .getLogger(MatLabConnection.class);
    private static final boolean debug = false;
    private MatlabProxy matlabProxy;
    private MatlabTypeConverter matlabTypeConverter;


    public MatLabConnection() throws MatlabConnectionException {
        LOG.info("Getting MatLab factory.");
        final MatlabProxyFactoryOptions matlabProxyFactoryOptions = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true)
                .setHidden(true)
                .setMatlabLocation(null)
                .build();
        final MatlabProxyFactory matlabProxyFactory = new MatlabProxyFactory(matlabProxyFactoryOptions);
        LOG.info("Getting MatLab proxy. Please be patient.");
        setMatLabProxy(matlabProxyFactory.getProxy());
        LOG.info("Getting MatLab matlabTypeConverter.");
        matlabTypeConverter = new MatlabTypeConverter(getMatLabProxy());
        if (debug) LOG.debug("Completed connection. MatLabTypeConverter = {}", matlabTypeConverter.toString());
    }

    public MatlabProxy getMatLabProxy() {
        return matlabProxy;
    }

    public void setMatLabProxy(final MatlabProxy matlabProxy) {
        this.matlabProxy = matlabProxy;
    }

    // Disconnect matlabProxy connection with MatLab
    public void DisconnectProxy() {
        getMatLabProxy().disconnect();
    }

    // Runs parameter command as eval command in MatLab instance
    public void eval(String command) throws MatlabInvocationException {
        try {
            getMatLabProxy().eval(command);
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
    }

    // Retrieves 2d numeric array form MatLab
    private double[][] getNumericArray2d(String var) throws MatlabInvocationException {
        MatlabNumericArray array = matlabTypeConverter.getNumericArray(var);
        return array.getRealArray2D();
    }

    // Retrieve boolean variable named parameter var from MatLab instance
    public Object getVariableBool(String var) throws MatlabInvocationException {
        try {
            return getMatLabProxy().getVariable(var);
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Used for fetching object arrays from MatLab
    public Object[] returningEval(String command) throws MatlabInvocationException {
        try {
            return getMatLabProxy().returningEval(command, 1);
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
        return new Object[0];
    }

    // Set boolean variable in MatLab instance
    public void setVariableBool(String var, int onOff) throws MatlabInvocationException {
        if (onOff != 1 && onOff != 0) {
            return;
        }
        getMatLabProxy().setVariable(var, onOff);
    }
}
