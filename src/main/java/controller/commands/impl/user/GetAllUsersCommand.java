package controller.commands.impl.user;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;
import model.entities.UserType;
import services.UserService;
import services.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;
import static utils.PaginationUtil.*;

@Log4j2
public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            int limit = getLimit(req);
            int[] pages = getPages(limit, service.getUserCount());
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);
            String sorting = getSortingType(req, User.class);

            req.setAttribute(USERS_ATTR, service.getAllUsers(limit, pages, currentPage, offset, sorting));
            req.setAttribute(USER_TYPES,
                    Arrays
                            .stream(UserType.values())
                            .map(Enum::name)
                            .collect(Collectors.toList()));

            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, pages);

            return PageConstants.USERS_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
