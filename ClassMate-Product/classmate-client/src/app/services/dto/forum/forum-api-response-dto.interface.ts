import { ForumDTO } from "./forum-dto.interface";
import { PostDTO } from "../post/post-dto.interface";

export interface ForumApiResponseDTO{
  forum: ForumDTO;
  posts: PostDTO[];
}
