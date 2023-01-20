package com.my.project.controller.commands.impl.user;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.PageConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.User;
import com.my.project.model.entities.UserType;
import com.my.project.services.UserService;
import com.my.project.services.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.my.project.utils.PaginationUtil.*;

@Log4j2
public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            int limit = getLimit(req);
            String sorting = getSortingType(req, User.class);
            String filter = getFilter(req, AttributeConstants.USER_TYPE_DB);
            Map<String, String[]> filters = filter.equals(AttributeConstants.NONE_ATTR)
                    ? new HashMap<>()
                    : new HashMap<String, String[]>() {{
                put(AttributeConstants.USER_TYPE_DB, new String[]{filter});
            }};
            int[] pages = getPages(limit, service.getUserCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            req.setAttribute(AttributeConstants.USERS_ATTR, service.getAllUsers(limit, offset, sorting, filters));

            req.setAttribute(AttributeConstants.USER_TYPES,
                    Arrays.stream(UserType.values())
                            .map(Enum::name)
                            .collect(Collectors.toList()));

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
