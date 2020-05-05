
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Web Demo</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>


</head>
<body>
    <c:if test="${empty firstStartToken}">
        <c:redirect url="serv"/>
    </c:if>
    <h3>${Error}</h3>
    <ul class="list-group" style="width:30%; cursor: pointer;">
        <c:forEach items="${aircompanies}" var="company">
            <li class="list-group-item" id="li${company.code}" onclick="clickOnCompany(${company.code})">
                <sapn class="company" id="company${company.code}"
                      style="cursor: pointer;">
                    Code: ${company.code} Name: ${company.name}
                </sapn>
                <ul class="list-group">
                     <c:forEach items="${company.flights}" var="flight">
                         <li class="list-group-item" id="li${flight.code}"
                             onclick="event.stopPropagation(); clickOnFlight(${flight.code})">
                             <span class="flight" parent="${company.code}" id="flight${flight.code}"
                                  style="cursor: pointer;">
                                 Code: ${flight.code} ${flight.from}-${flight.to}
                             </span>
                         </li>
                     </c:forEach>
                </ul>
            </li>
        </c:forEach>
    </ul>
    <form method="post" action="serv" id="companyForm" style="width:30%; margin:20px; float:left; padding:10px; border: 2px solid LightGray;border-radius:5px;">
        <h2>Company:</h2>
        <div class="form-group">
            <label for="exampleFormControlSelect2">Code:</label>
            <input type="text" id="companyCode" name="newCode" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="exampleFormControlSelect2">Name:</label>
            <input type="text" id="companyName" name="newName" class="form-control"/>
        </div>
        <input type="hidden" name="command" id="companyCommand" >
        <input type="hidden" name="code" id="companyCodeSelected">

        <input onclick="submitCommand('addCompany', 'companyForm', 'companyCommand')" type="button" value="Add" class="btn btn-primary mb-2" />
        <input onclick="submitCommand('updateCompany', 'companyForm', 'companyCommand')" type="button" value="Update" class="btn btn-info mb-2" />
        <input onclick="submitCommand('deleteCompany', 'companyForm', 'companyCommand')" type="button" value="Delete" class="btn btn-danger mb-2" />
    </form>

    <form method="post" action="serv" id="flightForm" style="width:30%; margin:20px; padding:10px;float:left;border: 2px solid LightGray;border-radius:5px;">
        <h2>Flight:</h2>
        <div class="form-group">
            <label for="exampleFormControlSelect2">Code:</label>
            <input type="text" id="flightCode" name="newCode" class="form-control" />
        </div>
        <div class="form-group">
            <label for="exampleFormControlSelect2">From:</label>
            <input type="text" id="flightFrom" name="newFrom" class="form-control" />
        </div>
        <div class="form-group">
            <label for="exampleFormControlSelect2">To:</label>
            <input type="text" id="flightTo" name="newTo" class="form-control" />
        </div>

        <input type="hidden" name="command" id="flightCommand" value="">
        <input type="hidden" name="code" id="flightCodeSelected" value="">
        <input type="hidden" name="airCode" id="companyAirCodeSelected" value="">

        <input onclick="submitCommand('addFlight', 'flightForm', 'flightCommand')" type="button"  value="Add" class="btn btn-primary mb-2" />
        <input onclick="submitCommand('updateFlight', 'flightForm', 'flightCommand')" type="button"  value="Update" class="btn btn-info mb-2" />
        <input onclick="submitCommand('deleteFlight', 'flightForm', 'flightCommand')" type="button"  value="Delete" class="btn btn-danger mb-2" />
    </form>
    <script>
        var prevFlightSelectedId = -1;
        var prevCompanySelectedId = -1;
        function clickOnCompany(companyCode){
            companies = document.getElementsByClassName("company");
            if(prevCompanySelectedId > 0){
                document.getElementById("li" + prevCompanySelectedId).style.background = "white";
            }
            if(prevFlightSelectedId > 0){
                document.getElementById("li" + prevFlightSelectedId).style.background = "white";
            }
            document.getElementById("li" + companyCode).style.background = "LightBlue";
            prevCompanySelectedId = companyCode;
            document.getElementById("companyCodeSelected").value = companyCode;
            document.getElementById("companyAirCodeSelected").value = companyCode;

        }
        function clickOnFlight(flightCode){
            flights = document.getElementsByClassName("flight");
            if(prevFlightSelectedId > 0){
                document.getElementById("li" + prevFlightSelectedId).style.background = "white";
            }
            if(prevCompanySelectedId > 0){
                document.getElementById("li" + prevCompanySelectedId).style.background = "white";
            }
            document.getElementById("li" + flightCode).style.background = "LightBlue";
            flight = document.getElementById("flight" + flightCode);
            parent = flight.getAttribute("parent");
            document.getElementById("flightCodeSelected").value = flightCode;
            prevFlightSelectedId = flightCode;
            document.getElementById("companyAirCodeSelected").value = parent;
        }
        function submitCommand(command, form, inputId){
            document.getElementById(inputId).value = command;
            document.getElementById(form).submit();
        }
    </script>
</body>
</html>

