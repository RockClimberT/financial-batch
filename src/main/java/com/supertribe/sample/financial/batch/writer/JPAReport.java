package com.supertribe.sample.financial.batch.writer;

import com.supertribe.sample.financial.batch.writer.entity.JpaInstrument;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("jpaReport")
public class JPAReport extends AbstractBatchlet {
    private static final Logger LOGGER = Logger.getLogger(JPAReport.class.getName());

    @Inject
    private JobContext context;

    @Override
    public String process() throws Exception {
        final Map<JpaInstrument.Id, JPAWriter.Item<JpaInstrument>> items = Map.class.cast(context.getTransientUserData());

        LOGGER.info("Added instruments");
        logStream(items.values().stream().filter(i -> i.isNew() != null && i.isNew()));

        LOGGER.info("Updated instruments");
        logStream(items.values().stream().filter(i -> i.isNew() != null && !i.isNew()));

        LOGGER.info("Stale instruments");
        logStream(items.values().stream().filter(i -> i.isNew() == null));

        return "reported";
    }

    private void logStream(final Stream<JPAWriter.Item<JpaInstrument>> itemStream) {
        itemStream.map(JPAWriter.Item::getValue).forEach(i -> LOGGER.info("  - " + i));
    }
}
