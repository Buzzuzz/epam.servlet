package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.UserServiceImpl;

import java.util.Map;

import static com.servlet.ejournal.utils.SqlUtil.*;

@Log4j2
public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            int limit = getLimit(req);
            String sorting = getSortingType(req, User.class);
            String filter = getFilter(req, AttributeConstants.USER_TYPE_DB);
            Map<String, String[]> filters = getFilters(req, AttributeConstants.USER_TYPE_DB);
            int[] pages = getPages(limit, service.getUserCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            req.setAttribute(AttributeConstants.USERS_ATTR, service.getAllUsers(limit, offset, sorting, filters));
            req.setAttribute(AttributeConstants.USER_TYPES, service.getAllUserTypes());
            req.setAttribute(AttributeConstants.SORTING_TYPE, sorting);
            req.setAttribute(AttributeConstants.DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(AttributeConstants.CURRENT_PAGE, currentPage);
            req.setAttribute(AttributeConstants.RECORDS, pages);
            req.setAttribute(AttributeConstants.USER_TYPE_DB, filter);

            return PageConstants.USERS_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
