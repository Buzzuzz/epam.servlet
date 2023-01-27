package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.UserService;

import java.util.Map;

import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        UserService service = context.getUserService();

        int limit = getLimit(req);
        String sorting = getSortingType(req, DEFAULT_USER_SORTING);
        String filter = getFilter(req, USER_TYPE_DB);
        Map<String, String[]> filters = getFilters(req, USER_TYPE_DB);
        int[] pages = getPages(limit, service.getUserCount(filters));
        int currentPage = Math.min(getCurrentPage(req), pages.length);
        int offset = getOffset(limit, currentPage);

        req.setAttribute(USERS_ATTR, service.getAllUsers(limit, offset, sorting, filters));
        req.setAttribute(USER_TYPES, service.getAllUserTypes());
        req.setAttribute(SORTING_TYPE, sorting);
        req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
        req.setAttribute(CURRENT_PAGE, currentPage);
        req.setAttribute(RECORDS, pages);
        req.setAttribute(USER_TYPE_DB, filter);

        return PageConstants.USERS_PAGE;
    }
}
