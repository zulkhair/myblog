import {useRouter} from "next/router";
import {MainPostView} from "@/components/Post";

export default function ShowPostDetailPage() {
    const router = useRouter();
    const {urlPath} = router.query;

    if (urlPath === undefined || Array.isArray(urlPath)) {
        return <div>Invalid post url path provided</div>;
    }

    return (
        <MainPostView urlPath={urlPath}/>
    );
}
