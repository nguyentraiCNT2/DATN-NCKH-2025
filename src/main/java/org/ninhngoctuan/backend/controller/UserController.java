    package org.ninhngoctuan.backend.controller;

    import org.ninhngoctuan.backend.controller.output.UserOutPut;
    import org.ninhngoctuan.backend.dto.ProfileDTO;
    import org.ninhngoctuan.backend.dto.UserDTO;
    import org.ninhngoctuan.backend.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.core.io.FileSystemResource;
    import org.springframework.core.io.Resource;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.File;
    import java.nio.file.Paths;
    import java.util.List;

    @RestController
    @RequestMapping("/user")
    public class UserController {
        @Value("${images.dir}")
        private String imagesDir;
        @Autowired
        private UserService userService;
        @GetMapping("/me")
        public ResponseEntity<?> me() {
            try {
                ProfileDTO dto = userService.me();
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @GetMapping("/userdetail/{id}")
        public ResponseEntity<?> userdetail(@PathVariable long id) {
            try {
                ProfileDTO userDTO = userService.profileUser(id);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @PostMapping("/unactive")
        public ResponseEntity<?> unactive() {
            try {
                UserDTO userDTO = userService.unActiveUser();
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }


        @PostMapping("/active/{id}")
        public ResponseEntity<?> active(@PathVariable Long id) {
            try {
                UserDTO userDTO = userService.ActiveUser(id);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @PostMapping("/block/active/{id}")
        public ResponseEntity<?> blockActive(@PathVariable Long id) {
            try {
                UserDTO userDTO = userService.blockUser(id);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @PutMapping("/changepassword")
        public ResponseEntity<?> changepassword(@RequestParam("oldPassword") String oldPassword
                , @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword) {
            try {
                userService.changePassword(oldPassword, newPassword, confirmPassword);
                return  ResponseEntity.status(HttpStatus.OK).body("Thay đổi thành công");
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }

        }

        @PutMapping("/update")
        public ResponseEntity<?> update(@RequestBody ProfileDTO userDTO) {
            try {
                UserDTO dto = userService.updateProfile(userDTO);
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @PostMapping("/upload/profile/picture")
        public ResponseEntity<?> profilePicture(@RequestParam("picture") MultipartFile file) {
            try {
                userService.updateProfilePicture(file);
                return ResponseEntity.status(HttpStatus.OK).body("Cập nhật ảnh đại diện thành công");
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @PostMapping("/upload/cover/picture")
        public ResponseEntity<?> coverPicture(@RequestParam("picture") MultipartFile file) {
            try {
                userService.updateCoverPicture(file);
                return ResponseEntity.status(HttpStatus.OK).body("Cập nhật ảnh bìa thành công");
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @GetMapping("/images/{filename}")
        public ResponseEntity<Resource> getImage(@PathVariable String filename) {
            try {
                File file = Paths.get(imagesDir).resolve(filename).toFile();
                if (file.exists()) {
                    Resource resource = new FileSystemResource(file);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Set the correct media type
                    headers.setContentType(MediaType.IMAGE_PNG); // Set the correct media type
                    headers.setContentType(MediaType.IMAGE_GIF); // Set the correct media type
                    headers.setContentDispositionFormData("attachment", filename);
                    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/getbyrole/{roleId}")
        public ResponseEntity<?> getbyrole(@PathVariable long roleId,@RequestParam("page") int page, @RequestParam("limit") int limit) {
            try {
                UserOutPut result = new UserOutPut();
                result.setPage(page);
                Pageable pageable =  PageRequest.of(page - 1, limit);
                result.setListResult(userService.getUserByRole(roleId,pageable));
                result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));

                return  ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @GetMapping("/by/{id}")
        public ResponseEntity<?> getbyrole(@PathVariable long id) {
            try {
             UserDTO userDTO = userService.getUserById(id);
                return  ResponseEntity.status(HttpStatus.OK).body(userDTO);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @PutMapping("/updaterole")
        public ResponseEntity<?> updateRole(@RequestParam("userId") long userid, @RequestParam("roleId") Long roleId) {
            try {
                UserDTO userDTO = userService.updateRole(userid, roleId);
                return  ResponseEntity.status(HttpStatus.OK).body(userDTO);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @GetMapping("/filter")
        public ResponseEntity<?> filterUsers(@RequestParam("fullName") String fullName,
                                             @RequestParam("email") String email, @RequestParam("phone") String phone,
                                             @RequestParam("isActive") boolean isActive, @RequestParam("isEmailActive") boolean isEmailActive,
                                             @RequestParam("page") int page, @RequestParam("limit") int limit) {
            try {
                UserOutPut result = new UserOutPut();
                result.setPage(page);
                Pageable pageable =  PageRequest.of(page - 1, limit);
                result.setListResult(userService.filterUsers(fullName,email,phone,isActive,isEmailActive,pageable));
                result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @GetMapping("/getall")
        public ResponseEntity<?> getAll (  @RequestParam("page") int page, @RequestParam("limit") int limit){
            try {
                UserOutPut result = new UserOutPut();
                result.setPage(page);
                Pageable pageable =  PageRequest.of(page - 1, limit);
                result.setListResult(userService.getAll(pageable));
                result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @GetMapping("/getall/people")
        public ResponseEntity<?> getAllPage (@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit){
            try {
                UserOutPut result = new UserOutPut();
                result.setPage(page);
                Pageable pageable =  PageRequest.of(page - 1, limit);
                result.setListResult(userService.getAllPage(pageable));
                result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        @GetMapping("/getbyfullname/{fullname}")
        public ResponseEntity<?> getByFullName ( @PathVariable String fullname ){
            try {
                List<UserDTO> result =  userService.getByName(fullname);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

    }
