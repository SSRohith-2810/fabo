function toggleSubMenu(submenuId) {
    var submenus = document.querySelectorAll(".submenu");
    submenus.forEach(function (submenu) {
      if (submenu.id !== submenuId) {
        submenu.classList.remove("show");
      }
    });
  
    var submenu = document.getElementById(submenuId);
    submenu.classList.toggle("show");
  }

  function openPopup(popupId) {
    var popup = document.getElementById(popupId);
    if (popup) {
        popup.style.display = "block";
    }
     $('#addUserPopup').show();
}
 $(document).ready(function() {
        openPopup();
    });

function closePopup() {
    isEditing = false;
    var popups = document.querySelectorAll(".popup");
    popups.forEach(function (popup) {
      popup.style.display = "none";
    });
  }


  function validateNumericInput(input) {
    // Remove non-numeric characters from the input value
    input.value = input.value.replace(/\D/g, '');
}
